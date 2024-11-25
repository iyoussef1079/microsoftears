/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import java.io.Serializable;

/**
 *
 * @author Utilisateur
 */
public class ControlPoint implements IDrawable, Serializable, Cloneable{
    private TearDropTrailer tearDrop;
    private PointMeasurement center;
    private CustomPolygon polygon;
    private Boolean moovable = true;
    private DisplayMode displayMode = DisplayMode.REGULAR;
    
    
    public ControlPoint(PointMeasurement center, TearDropTrailer tearDrop){
        this.tearDrop = tearDrop;
        this.center = center;
    }
    

    @Override
    public CustomPolygon getPolygon() {
        EllipseArc e = new EllipseArc(center, new Measurement(1, 1), new Measurement(1, 1));
        return e.getPolygon();
    }

    @Override
    public void invalidatePolygon() {
        this.polygon = null;
             
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
        return 0;
    }

    @Override
    public DisplayMode getDisplayMode() {
        return this.displayMode;
    }

    @Override
    public void moove(PointMeasurement point) {
        this.center = point;
        invalidatePolygon();
    }

    public PointMeasurement getCenter() {
        return this.center;
    }

    public void setCenter(PointMeasurement point) {
        this.center = point;
    }

    public void addControlPoint(PointMeasurement point){
        this.tearDrop.addControlPoint(point);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
