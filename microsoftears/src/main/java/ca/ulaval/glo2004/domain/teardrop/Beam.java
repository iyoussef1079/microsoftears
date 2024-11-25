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
import java.util.List;

/**
 *
 * @author Duroy
 */

public class Beam implements IDrawable, Cloneable, Serializable {

    private CustomPolygon polygon = null;
    private Measurement height;
    private Measurement width;
    private TearDropTrailer tearDropTrailer;
    private PointMeasurement topLeftPosition;
    private Measurement topLeftPositionX;
    private int vertexCount;
    private DisplayMode displayMode;
    private boolean moovable = true;

    public Beam(TearDropTrailer tearDropTrailer) {
        // Default values
        height = new Measurement(50.8); // 2 inches is 50.8 millimeters
        width = new Measurement(50.8);
        this.tearDropTrailer = tearDropTrailer;
        setTopLeftPositionX(new Measurement(0, 1));
        this.topLeftPosition = null;
        this.polygon = null;
        this.displayMode = DisplayMode.REGULAR;

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
        if (this.topLeftPosition == null) {
            calculateTopLeftPosition();
        }
        return topLeftPosition;
    }

    public Measurement getTopLeftPositionX() {
        return topLeftPositionX;
    }

    public void setTopLeftPositionX(Measurement topLeftPositionX) {
        this.topLeftPositionX = topLeftPositionX;
        this.topLeftPosition = null;
    }

    private void calculateTopLeftPosition() {
        List<PointMeasurement> table = tearDropTrailer.getCurvedProfile().getPolygon().getVertices();
        for (int i = 0; i < table.size(); i++) {
            if (i > 0) {
                if ((table.get(i).getX().compareTo(topLeftPositionX) >= 0) && (table.get(i - 1).getX().compareTo(topLeftPositionX) <= 0)) {
                    this.topLeftPosition = table.get(i);
                    break;
                }
            }
        }
        if (this.topLeftPosition == null) {
            throw new UserException("Position invalide de la poutre arriere.");
        }
    }

    @Override
    public CustomPolygon getPolygon() {
        if (polygon == null) {
            polygon = CalculatePolygon();
        }
        return polygon;//To change body of generated methods, choose Tools | Templates.
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public CustomPolygon CalculatePolygon() {

        //calculatePosition();
        PointMeasurement p = this.getTopLeftPosition();
        PointMeasurement p1 = null;
        PointMeasurement p2 = null;
        PointMeasurement p3 = null;
        PointMeasurement pi = null;
        Measurement constante = height.div(width);
        List<PointMeasurement> table = tearDropTrailer.getCurvedProfile().getPolygon().getVertices();

        if (p == null) {
            throw new UserException("La poutre arriere ne peut pas se placer ici");
        }
        for (int i = 0; i < table.size(); i++) {
            if (((table.get(i).sub(p)).norm().compareTo(width) == 1) && (table.get(i).getX().compareTo(p.getX()) == 1)) {
                p1 = table.get(i);
                break;
            }
        }
        if (p1 == null) {
            throw new UserException("La poutre arriere ne peut pas se placer ici");
        }
        pi = p.sub(p1);
        p2 = new PointMeasurement(p.getX().add(pi.getY().mult(constante)), p.getY().sub(pi.getX().mult(constante)));
        p3 = new PointMeasurement(p2.getX().sub(pi.getX()), p2.getY().sub(pi.getY()));
        List<PointMeasurement> arraytemp = new ArrayList<>();
        arraytemp.add(p);
        arraytemp.add(p1);
        arraytemp.add(p3);
        arraytemp.add(p2);
        arraytemp.add(p);

        return new CustomPolygon(arraytemp);

    }

    @Override
    public void invalidatePolygon() {
        topLeftPosition = null;
        polygon = null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        vertexCount = verticesPerInch;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        if (this.moovable) {
            this.setTopLeftPositionX(point.x);
        }
    }

}
