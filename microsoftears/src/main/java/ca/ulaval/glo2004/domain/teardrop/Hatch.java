package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;

import java.io.Serializable;
import java.util.*;

public class Hatch implements IDrawable, Cloneable, Serializable {
    private TearDropTrailer tearDropTrailer;
    public Measurement thickness;
    public Measurement distanceBeam;
    public Measurement distanceFloor;
    public double deadWeightPounds;
    public Measurement curveRadius;
    public int vertexCount;
    private ArrayList<PointMeasurement> outerVertices;
    private ArrayList<PointMeasurement> innerVertices;
    public CustomPolygon polygon;
    private boolean moovable = false;
    private PointMeasurement hingeCenterPosition;
    
    protected DisplayMode displayMode;
    protected int verticesPerInch;
    
    public Hatch(TearDropTrailer tearDropTrailer){
        // Default values
        this.tearDropTrailer = tearDropTrailer;
        this.thickness = new Measurement(2,1);
        this.distanceBeam = new Measurement(5,16);
        this.distanceFloor = new Measurement(3,8);
        this.curveRadius = new Measurement(3,1); // No idea what's the default value here...
        this.deadWeightPounds = 50.0;

        this.polygon = null;
        this.hingeCenterPosition = null;
        this.displayMode = DisplayMode.REGULAR;
        this.verticesPerInch = DEFAULT_VERTICES_PER_INCH;
    }
    
    public TearDropTrailer getTearDropTrailer() {
        return tearDropTrailer;
    }

    public double getDeadWeightPounds() {
        return deadWeightPounds;
    }

    public void setDeadWeightPounds(double deadWeightPounds) {
        this.deadWeightPounds = deadWeightPounds;
    }

    public PointMeasurement getHingeCenterPosition() {
        if (this.hingeCenterPosition == null) {
            this.getPolygon();
        }
        return this.hingeCenterPosition;
    }

    public Measurement getCurveRadius() {
        return curveRadius;
    }
    
    public Measurement getThickness() {
        return thickness;
    }

    public void setThickness(Measurement thickness) {
        this.thickness = thickness;
    }
    
    public Measurement getDistanceBeam() {
        return distanceBeam;
    }

    public void setDistanceBeam(Measurement distanceBeam) {
        this.distanceBeam = distanceBeam;
    }
    
    public Measurement getDistanceFloor() {
        return distanceFloor;
    }

    public void setDistanceFloor(Measurement distanceFloor) {
        this.distanceFloor = distanceFloor;
    }

    public void setCurveRadius(Measurement curveRadius) {
        this.curveRadius = curveRadius;
    }

    /**
     * Method used in calculations for strut selection
     * @return the overall hatch length
     */
    public Measurement getOverallLength() {
        if (this.polygon == null) {
            this.getPolygon();
        }
        int bottomLeftCornerIndex = this.tearDropTrailer.getCurvedProfile().getBottomLeftCornerIndex();
        PointMeasurement bottomLeftCorner = this.tearDropTrailer.getCurvedProfile().getPolygon().getVertices().get(bottomLeftCornerIndex);
        return GeometricUtils.getDistance(bottomLeftCorner, getHingeCenterPosition());
    }

    public ArrayList<PointMeasurement> getOuterVertices() {
        if (this.outerVertices == null) {
            getPolygon();
        }
        return this.outerVertices;
    }

    public ArrayList<PointMeasurement> getInnerVertices() {
        if (this.innerVertices == null) {
            getPolygon();
        }
        return this.innerVertices;
    }

    @Override
    public CustomPolygon getPolygon() {
        // this.profilePolygon must be ordered clockWise in order for this algo
        // to work
        if (this.displayMode == DisplayMode.REGULAR || this.displayMode == DisplayMode.HIGH_LIGHT) {
            if (this.polygon == null) {
                int bottomLeftCornerIndex = this.tearDropTrailer.getCurvedProfile().getBottomLeftCornerIndex();
                PointMeasurement bottomLeftCornerRawProfile = this.getTearDropTrailer().getRawProfile().getPolygon().getVertices().get(3);
                PointMeasurement bottomLeftCorner = this.getTearDropTrailer().getCurvedProfile().getPolygon().getVertices().get(bottomLeftCornerIndex);
                PointMeasurement firstOuterHatchVertex = new PointMeasurement(
                        bottomLeftCornerRawProfile.x.add(tearDropTrailer.getFloor().getBackmargin().sub(distanceFloor)),
                        bottomLeftCorner.y
                );

                // Find beam position
                 PointMeasurement beamTopLeft = this.tearDropTrailer.getBeam().getTopLeftPosition();

                // Find all outer hatch vertices
                this.outerVertices = new ArrayList<>(Arrays.asList(firstOuterHatchVertex, bottomLeftCorner));
                PointMeasurement currentVertex = this.getTearDropTrailer().getCurvedProfile().getPolygon().getVertices().get(bottomLeftCornerIndex + 1);
                int i = bottomLeftCornerIndex + 2;//1
                List<PointMeasurement> profileVertices = this.getTearDropTrailer().getCurvedProfile().getPolygon().getVertices();

                PointMeasurement endHatchPosition = new PointMeasurement(
                        beamTopLeft.getX().sub(this.distanceBeam),
                        beamTopLeft.getY()
                );

                // 100 mm seems fair
                while(endHatchPosition.sub(currentVertex).norm().compareTo(new Measurement(10)) > 0){
                    this.outerVertices.add(currentVertex);
                    currentVertex = profileVertices.get(i);
                    i = (i + 1) % profileVertices.size();
                }

                // computer center hinge position for strut positioning
                this.hingeCenterPosition = endHatchPosition.add(beamTopLeft).div(2);

                this.innerVertices = new ArrayList<>();
                Measurement limitX = this.getTearDropTrailer().getRawProfile().getPolygon().getVertices().get(3).x.add(this.thickness);
                Measurement lowerLimitY = this.getTearDropTrailer().getRawProfile().getPolygon().getVertices().get(3).y.sub(this.thickness);
                Measurement upperLimitY = this.getTearDropTrailer().getRawProfile().getPolygon().getVertices().get(0).y.add(this.thickness);
                for (int j = 0; j < this.outerVertices.size() - 2; j++) {
                    PointMeasurement startPoint = this.outerVertices.get(j);
                    PointMeasurement midPoint = this.outerVertices.get(j + 1);
                    PointMeasurement endPoint = this.outerVertices.get(j + 2);

                    PointMeasurement parallelVector = endPoint.sub(startPoint);
                    PointMeasurement normalVector = parallelVector.div(parallelVector.norm()); // vector norm of 1
                    PointMeasurement perpendicularVector = new PointMeasurement(
                            normalVector.getY().mult(-1),
                            normalVector.getX()
                    ); // 90 degrees rotation matrix

                    PointMeasurement newVertex = midPoint.add(perpendicularVector.mult(this.thickness));
                    if(newVertex.x.compareTo(limitX) != -1 && newVertex.y.compareTo(lowerLimitY) != 1 && newVertex.y.compareTo(upperLimitY) != -1){
                        innerVertices.add(newVertex);
                    }
                }
                if (this.innerVertices.size() > 0) {
                    // If thickness is valid
                    this.innerVertices.add(1, this.outerVertices.get(0).add(
                            new PointMeasurement(Measurement.zero(), thickness.mult(-1))
                    ));
                }
                
                List<PointMeasurement> innerVerticesToRemove = new ArrayList<>();
                innerVertices.parallelStream().forEach(vertex -> {
                    outerVertices.parallelStream().map(point -> vertex.y.sub(point.y).power2().add(vertex.x.sub(point.x).power2()).sqrt()).filter(distance -> (distance.compareTo(this.thickness) == -1)).forEachOrdered(_item -> {
                        innerVerticesToRemove.add(vertex);
                    });
                });
                innerVertices.removeAll(innerVerticesToRemove);

                
                this.polygon = getHatchWithCurve(this.outerVertices, this.innerVertices, curveRadius);
            }
            return this.polygon;
        } else {
            return new CustomPolygon();
        }
    }

    @Override
    public void invalidatePolygon() {
        this.polygon = null;
        this.hingeCenterPosition = null;
        this.innerVertices = null;
        this.outerVertices = null;
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        this.verticesPerInch = verticesPerInch;
        this.invalidatePolygon();
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getVertexCount() {
        return this.vertexCount;
    }

    /**
     * This method finds all vertices in clockwise order
     * @param outerHatchVertices
     * @param innerVertices
     * @param curveRadius
     * @return
     */
    protected CustomPolygon getHatchWithCurve(List<PointMeasurement> outerHatchVertices,
            List<PointMeasurement> innerVertices, Measurement curveRadius){
        // TODO: Add top curve
        List<PointMeasurement> fullHatch = new ArrayList<>();

        fullHatch.addAll(outerHatchVertices);

        // find curve position
        if (curveRadius.compareTo(thickness) >= 0) {
            PointMeasurement circleCenter = null;
            PointMeasurement lastOuterVertex = outerHatchVertices.get(outerHatchVertices.size() - 1);
            PointMeasurement perpendicularVector = null;
            for (int i = innerVertices.size() - 1; i > 0; i--) {
                PointMeasurement parallelVector = innerVertices.get(i).sub(innerVertices.get(i - 1));
                perpendicularVector = new PointMeasurement(
                        parallelVector.getY(),
                        parallelVector.getX().mult(-1)
                ).div(parallelVector.norm());
                circleCenter = innerVertices.get(i).add(perpendicularVector.mult(curveRadius));
                if (GeometricUtils.getDistance(circleCenter, lastOuterVertex).compareTo(curveRadius) >= 0) {
                    break;
                }
            }
            PointMeasurement v1 = circleCenter.sub(lastOuterVertex);
            double startAngle = Math.atan2(-v1.getY().getRoundedInches(), -v1.getX().getRoundedInches());
            double endAngle = Math.atan2(-perpendicularVector.getY().getRoundedInches(), -perpendicularVector.getX().getRoundedInches());
            EllipseArc hatchTopCurve = new EllipseArc(circleCenter, curveRadius, curveRadius, startAngle, endAngle);
            List<PointMeasurement> hatchTopCurveVertices = hatchTopCurve.getPolygon().getVertices();


            fullHatch.addAll(hatchTopCurve.getPolygon().getVertices());

            // remove innerVertices overflowing curve
            int i = innerVertices.size() - 1;
            while (innerVertices.get(i).getX().compareTo(hatchTopCurveVertices.get(hatchTopCurveVertices.size() - 1).getX()) >= 0) {
                i--;
            }
            while (i > 0) {
                fullHatch.add(innerVertices.get(i));
                i--;
            }
        }


        fullHatch.add(outerHatchVertices.get(0));

        return new CustomPolygon(fullHatch);
    }
    
    public DisplayMode getDisplayMode() {
        return this.displayMode;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void moove(PointMeasurement point) {
    }

}
