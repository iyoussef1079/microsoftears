package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * The coordinate (0, 0) are located at the center of the RawProfile.
 * The coordinate system is the following:
 *    (0,0) +--------------> x
 *          |
 *          |
 *          |
 *          |
 *          |
 *        y v
 */
public class RawProfile implements IDrawable, Cloneable, Serializable {

    private TearDropTrailer tearDropTrailer;
    private Measurement height;
    private Measurement width;
    private DisplayMode displayMode;
    private CustomPolygon polygon;
    private int vertexcount;
    private PointMeasurement centre = new PointMeasurement(new Measurement(0), new Measurement(0));

    public RawProfile(TearDropTrailer tearDropTrailer) {
        this.tearDropTrailer = tearDropTrailer;
        this.height = new Measurement(48, 1);
        this.width = new Measurement(96, 1);
        centre = new PointMeasurement(new Measurement(0), new Measurement(0));
        this.displayMode = DisplayMode.HIDDEN;
        this.calculatePolygon();
    }

    private void calculatePolygon()
    {
        List<PointMeasurement> arraytemp = new ArrayList<>();
        arraytemp.add(new PointMeasurement(centre.x.sub(width.div(2)),centre.y.sub(height.div(2) ))); // Upper left
        arraytemp.add(new PointMeasurement(centre.x.add(width.div(2)),centre.y.sub(height.div(2) ))); // Upper right
        arraytemp.add(new PointMeasurement(centre.x.add(width.div(2)),centre.y.add(height.div(2) ))); // Lower right
        arraytemp.add(new PointMeasurement(centre.x.sub(width.div(2)),centre.y.add(height.div(2) ))); // Lower left
        arraytemp.add(new PointMeasurement(centre.x.sub(width.div(2)),centre.y.sub(height.div(2) )));
        this.polygon = new CustomPolygon(arraytemp);
    }

    @Override
    public CustomPolygon getPolygon() {
        if (this.polygon == null) {
            this.calculatePolygon();
        }
        return this.polygon;
    }

    public PointMeasurement getCentre() {
        return centre;
    }

    public void setCentre(PointMeasurement centre) {
        this.centre = centre;
    }

    @Override
    public void invalidatePolygon() {
        this.polygon = null;
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        // Does nothing, we always use 5 vertices
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public Measurement getHeight() {
        return height;
    }

    public void setHeight(Measurement height) {
        Measurement oldHeight = this.height;
        this.height = height;
        if (this.tearDropTrailer.getCurvedProfile().getClass().equals(EllipseCurvedProfile.class)) {
            this.updateEllipsesControlPoints(height.sub(oldHeight).div(2), Measurement.zero());
        }
    }

    public Measurement getWidth() {
        return width;
    }

    public void setWidth(Measurement width) {
        Measurement oldWidth = this.width;
        this.width = width;
        if (this.tearDropTrailer.getCurvedProfile().getClass().equals(EllipseCurvedProfile.class)) {
            this.updateEllipsesControlPoints(Measurement.zero(), width.sub(oldWidth).div(2));
        }
    }

    private void updateEllipsesControlPoints(Measurement heightDelta, Measurement widthDelta) {
        PointMeasurement topLeftCenter = this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.TOP_LEFT).getCenter();
        this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.TOP_LEFT).setCenter(
                new PointMeasurement(topLeftCenter.getX().sub(widthDelta), topLeftCenter.getY().sub(heightDelta))
        );
        PointMeasurement topRightCenter = this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.TOP_RIGHT).getCenter();
        this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.TOP_RIGHT).setCenter(
                new PointMeasurement(topRightCenter.getX().add(widthDelta), topRightCenter.getY().sub(heightDelta))
        );
        PointMeasurement bottomLeftCenter = this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.BOTTOM_LEFT).getCenter();
        this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.BOTTOM_LEFT).setCenter(
                new PointMeasurement(bottomLeftCenter.getX().sub(widthDelta), bottomLeftCenter.getY().add(heightDelta))
        );
        PointMeasurement bottomRightCenter = this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.BOTTOM_RIGHT).getCenter();
        this.tearDropTrailer.getEllipsesControlPoints().get(Quadrant.BOTTOM_RIGHT).setCenter(
                new PointMeasurement(bottomRightCenter.getX().add(widthDelta), bottomRightCenter.getY().add(heightDelta))
        );
    }

    @Override
    public int getVertexCount() {
        return vertexcount;
    }

    @Override
    public DisplayMode getDisplayMode() {
        return this.displayMode;
    }


    public PointMeasurement getCornerPosition(Quadrant quadrant) {
        PointMeasurement cornerPosition;
        switch (quadrant) {
            case TOP_LEFT:
                cornerPosition = new PointMeasurement(
                        this.width.div(2).mult(-1),
                        this.height.div(2).mult(-1)
                );
                break;
            case TOP_RIGHT:
                cornerPosition = new PointMeasurement(
                        this.width.div(2),
                        this.height.div(2).mult(-1)
                );
                break;
            case BOTTOM_LEFT:
                cornerPosition = new PointMeasurement(
                        this.width.div(2).mult(-1),
                        this.height.div(2)
                );
                break;
            case BOTTOM_RIGHT:
                cornerPosition = new PointMeasurement(
                        this.width.div(2),
                        this.height.div(2)
                );
                break;
            default:
                throw new RuntimeException("Invalid quadrant");
        }
        return cornerPosition;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void moove(PointMeasurement point) {
    }

}
