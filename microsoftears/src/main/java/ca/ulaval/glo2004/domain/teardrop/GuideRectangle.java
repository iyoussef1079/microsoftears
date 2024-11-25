package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GuideRectangle implements IDrawable, Cloneable, Serializable {
    private Measurement height;
    private Measurement width;
    private PointMeasurement topLeftPosition;
    private DisplayMode displayMode;
    private CustomPolygon polygon;
    private TearDropTrailer tearDropTrailer;

    public GuideRectangle(TearDropTrailer tearDropTrailer) {
        this.tearDropTrailer = tearDropTrailer;
        this.width = new Measurement(60, Unit.INCHES);
        this.height = new Measurement(6, Unit.INCHES);
        this.topLeftPosition = new PointMeasurement(new Measurement(-30, Unit.INCHES), new Measurement(15, Unit.INCHES));
        this.displayMode = DisplayMode.HIDDEN;
        this.polygon = null;
    }

    @Override
    public CustomPolygon getPolygon() {
        if (this.polygon == null) {
            List<PointMeasurement> vertices = new ArrayList<>();
            vertices.add(topLeftPosition);
            vertices.add(topLeftPosition.add(new PointMeasurement(width, Measurement.zero())));
            vertices.add(topLeftPosition.add(new PointMeasurement(width, height)));
            vertices.add(topLeftPosition.add(new PointMeasurement(Measurement.zero(), height)));
            vertices.add(topLeftPosition);
            polygon = new CustomPolygon(vertices);
        }
        return this.polygon;
    }

    @Override
    public void invalidatePolygon() {
        polygon = null;
    }

    public Measurement getHeight() {
        return height;
    }

    public void setHeight(Measurement height) {
        this.height = height;
    }

    public Measurement getWidth() {
        return width;
    }

    public void setWidth(Measurement width) {
        this.width = width;
    }

    public PointMeasurement getTopLeftPosition() {
        return topLeftPosition;
    }

    public void setTopLeftPosition(PointMeasurement topLeftPosition) {
        this.topLeftPosition = topLeftPosition;
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {

    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getVertexCount() {
        return 5;
    }

    @Override
    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    @Override
    public void moove(PointMeasurement point) {
        this.topLeftPosition = point;
    }
}
