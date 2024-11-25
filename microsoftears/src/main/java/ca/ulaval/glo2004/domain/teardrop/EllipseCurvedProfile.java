/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import ca.ulaval.glo2004.domain.controller.UserException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Duroy
 */
public class EllipseCurvedProfile extends CurvedProfile implements Serializable {
    public static final int DEFAULT_VERTICAL_RADIUS_INCHES = 4;//4
    public static final int DEFAULT_HORIZONTAL_RADIUS_INCHES = 4;//8
    private DisplayMode displayMode;
    protected TearDropTrailer tearDropTrailer;
    protected Map<Quadrant, Measurement> horizontalRadius;
    protected Map<Quadrant, Measurement> verticalRadius;
    protected CustomPolygon polygon;
    protected int bottomLeftCornerIndex = -1;
    private boolean moovable = false;

    public EllipseCurvedProfile(TearDropTrailer tearDropTrailer) {
        this.tearDropTrailer = tearDropTrailer;
        this.displayMode = DisplayMode.REGULAR;

        // default radius
        this.horizontalRadius = new EnumMap<>(Quadrant.class);
        this.verticalRadius = new EnumMap<>(Quadrant.class);
        for (Quadrant quadrant : Quadrant.values()) {
            this.horizontalRadius.put(quadrant, new Measurement(DEFAULT_HORIZONTAL_RADIUS_INCHES, 1));
            this.verticalRadius.put(quadrant, new Measurement(DEFAULT_VERTICAL_RADIUS_INCHES, 1));
        }
    }

    protected void calculatePolygon() {
        // TODO: check intersections
        CustomPolygon rawProfilePolygon = this.tearDropTrailer.getRawProfile().getPolygon();

        List<List<PointMeasurement>> cornerVertices = new ArrayList<>();

        this.getEllispeMap().entrySet().parallelStream().map(entry -> {
            EllipseArc ellipse = entry.getValue();
            List<PointMeasurement> ellipseVertices = ellipse.getPolygon().getVertices();

            // get intersections between an ellipse and the raw profile
            List<PointMeasurement> intersections = new ArrayList<>();
            List<Integer> intersectionIndexes = new ArrayList<>();
            for (int i = 0; i < ellipseVertices.size() - 1; i++) {
                PointMeasurement intersection = GeometricUtils
                        .getSegmentsIntersection(
                                rawProfilePolygon,
                                new SegmentMeasurement(ellipseVertices.get(i), ellipseVertices.get(i + 1))
                        );
                if (intersection != null) {
                    intersections.add(intersection);
                    intersectionIndexes.add(i);
                    if (intersections.size() == 2) break;
                }
            }

            List<PointMeasurement> out = new ArrayList<>();
            if (intersections.size() < 2) {
                throw new UserException("Problem with the ellipse profile: Ellipse incorrect position.");
                //out.addAll(ellipseVertices);
            } else {

                //out.add(intersections.get(0));
                out.addAll(ellipseVertices.subList(intersectionIndexes.get(0) + 1, intersectionIndexes.get(1)));
                //out.add(intersections.get(1));
            }
            // out.addAll(ellipseVertices);
            return out;
        }).parallel().forEachOrdered(toAdd -> {
            cornerVertices.add(toAdd);
        });


        List<PointMeasurement> vertices = new ArrayList<>();

        for (int i = 0; i < cornerVertices.size(); i++) {
            if (i == 3) {
                this.bottomLeftCornerIndex = vertices.size() - 1;
            }
            vertices.addAll(cornerVertices.get(i));
            vertices.addAll(GeometricUtils.sampleSegmentVertices(
                    new SegmentMeasurement(
                            cornerVertices.get(i).get(cornerVertices.get(i).size() - 1),
                            cornerVertices.get((i + 1) % cornerVertices.size()).get(0)
                    ),
                    1 // TODO: change to real vertex per inch //normally 10
            ));
        }

        // make sure we correctly close the polygon
        vertices.remove(vertices.size() - 1);
        vertices.add(vertices.get(0));
        this.polygon = new CustomPolygon(vertices);
    }

    public void setEllipse(Quadrant quadrant, Measurement verticalRadius, Measurement horizontalRadius) {
        this.horizontalRadius.replace(quadrant, horizontalRadius);
        this.verticalRadius.replace(quadrant, verticalRadius);
    }

    protected int getIntersectionsCount(EllipseArc ellipseArc, CustomPolygon polygon) {

        List<PointMeasurement> ellipseVertices = ellipseArc.getPolygon().getVertices();

        // get intersections between an ellipse and the raw profile
        List<PointMeasurement> intersections = new ArrayList<>();
        for (int i = 0; i < ellipseVertices.size() - 1; i++) {
            PointMeasurement intersection = GeometricUtils
                    .getSegmentsIntersection(
                            polygon,
                            new SegmentMeasurement(ellipseVertices.get(i), ellipseVertices.get(i + 1))
                    );
            if (intersection != null) {
                intersections.add(intersection);
            }
        }
        return intersections.size();
    }

    @Override
    public CustomPolygon getPolygon() {
        if (this.polygon == null) {
            this.calculatePolygon();
        }
        return this.polygon;
    }

    @Override
    public int getBottomLeftCornerIndex() {
        if (this.bottomLeftCornerIndex == -1) {
            this.calculatePolygon();
        }
        return this.bottomLeftCornerIndex;
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getVertexCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void invalidatePolygon() {
        this.polygon = null;
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DisplayMode getDisplayMode() {
        return this.displayMode;
    }

    public Map<Quadrant, EllipseArc> getEllispeMap() {
        Map<Quadrant, EllipseArc> ellipsesMap = new EnumMap<>(Quadrant.class);

        for (Quadrant quadrant : Quadrant.values()) {
            double[] angleRange = GeometricUtils.getAngleRangeFromQuadrant(quadrant);
            ellipsesMap.put(quadrant, new EllipseArc(
                    this.tearDropTrailer.getEllipsesControlPoints().get(quadrant).getCenter(),
                    this.horizontalRadius.get(quadrant),
                    this.verticalRadius.get(quadrant),
                    angleRange[0],
                    angleRange[1]
            ));
        }
        return ellipsesMap;
    }

    @Override
    public void moove(PointMeasurement point) {
    
    }


}
