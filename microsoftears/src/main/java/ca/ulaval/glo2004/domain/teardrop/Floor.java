/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Duroy
 */
public class Floor implements IDrawable, Cloneable, Serializable {
    private TearDropTrailer tearDropTrailer;
    private Measurement thickness;
    private Measurement frontmargin;
    private Measurement backmargin;
    private CustomPolygon polygon;
    private int vertexCount;
    private DisplayMode displayMode;
    private boolean moovable = true;
    public Floor(TearDropTrailer tearDropTrailer,Measurement thickness, Measurement frontmargin, Measurement backmargin)
    {
        this.tearDropTrailer = tearDropTrailer;
        this.backmargin = backmargin;
        this.frontmargin = frontmargin;
        this.thickness = thickness;
    }
    
    public Floor(TearDropTrailer tearDropTrailer)
    {
        // Default Values
        this.tearDropTrailer = tearDropTrailer;
        this.backmargin = new Measurement(10,1);
        this.frontmargin = new Measurement(5,1);
        this.thickness = new Measurement(5,4);
        polygon = calculatePolygon();
        this.displayMode = DisplayMode.REGULAR;
    }

    public Measurement getThickness() {
        return thickness;
    }

    public void setThickness(Measurement thickness) {
        this.thickness = thickness;
    }

    public Measurement getFrontmargin() {
        return frontmargin;
    }

    @Override
    public CustomPolygon getPolygon() {
        if(polygon == null) return calculatePolygon();
        else
        return polygon; //To change body of generated methods, choose Tools | Templates.
    }

    public void setFrontmargin(Measurement frontmargin) {
        this.frontmargin = frontmargin;
    }

    public Measurement getBackmargin() {
        return backmargin;
    }

    public void setBackmargin(Measurement backmargin) {
        this.backmargin = backmargin;
    }

    @Override
    public void invalidatePolygon() {
        polygon = null; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        vertexCount = verticesPerInch; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getVertexCount() {
        return vertexCount; //To change body of generated methods, choose Tools | Templates.
    }
    private CustomPolygon calculatePolygon()
    {
        CustomPolygon sideWallPolygon  = tearDropTrailer.getRawProfile().getPolygon();
        PointMeasurement lowerFrontCorner = sideWallPolygon.getVertices().get(2);
        PointMeasurement lowerBackCorner = sideWallPolygon.getVertices().get(3);
        List<PointMeasurement> arraytemp = new ArrayList<>();
        
        arraytemp.add(new PointMeasurement(lowerBackCorner.getX().add(backmargin), lowerBackCorner.getY().sub(thickness)));
        arraytemp.add(new PointMeasurement(lowerFrontCorner.getX().sub(frontmargin), lowerFrontCorner.getY().sub(thickness)));
        arraytemp.add(new PointMeasurement(lowerFrontCorner.getX().sub(frontmargin), lowerFrontCorner.getY()));
        arraytemp.add(new PointMeasurement(lowerBackCorner.getX().add(backmargin), lowerBackCorner.getY()));
        arraytemp.add(new PointMeasurement(lowerBackCorner.getX().add(backmargin), lowerBackCorner.getY().sub(thickness)));
      
        CustomPolygon temppolygon  = new CustomPolygon(arraytemp);
        return temppolygon;
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
        Measurement actualFront = this.frontmargin;
        Measurement actualBack = this.backmargin;
        // convertion vers les dimensions de la roulotte
        Measurement convertPoint = new Measurement(0);
        
        
        
        if(this.moovable){
            //this.setFrontmargin(actualFront.sub(point.x.mult(0.01)));
            //this.setBackmargin(actualFront.add(point.x.mult(0.01)));
        }
    }

}
