/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import java.io.Serializable;
import java.util.ArrayList;
import java.lang.Math;
import java.util.List;

/**
 *
 * @author Duroy
 */
public class SideOpening implements IDrawable, Cloneable, Serializable {
    protected CustomPolygon polygon;
    private int vertexperinches;
    private DisplayMode displaymode;
    private TearDropTrailer teardroptrailer;
    private Measurement height;
    private Measurement width;
    private Measurement rayon;
    private PointMeasurement topLeftPosition;
    private boolean moovable = true;

    public SideOpening(TearDropTrailer teardroptrailer) {
        this.teardroptrailer = teardroptrailer;
        this.height = new Measurement(25,1);
        this.width = new Measurement(15,1);
        this.rayon = new Measurement(4,2);
        this.topLeftPosition = new PointMeasurement(new Measurement(15,1),new Measurement(-5,1));
        displaymode = DisplayMode.REGULAR;
        vertexperinches = 5;
        this.calculatePolygon();
    }
    
    @Override
    public DisplayMode getDisplayMode() {
        return displaymode;
    }

    public SideOpening(TearDropTrailer teardroptrailer, Measurement height, Measurement width, Measurement rayon, PointMeasurement topLeftPosition) {
        this.teardroptrailer = teardroptrailer;
        this.height = height;
        this.width = width;
        this.rayon = rayon;
        this.topLeftPosition = topLeftPosition;
        vertexperinches = 10;
        this.calculatePolygon();
        displaymode = DisplayMode.REGULAR;
    }

    public void setHeight(Measurement height) {
        this.height = height;
        
    }

    public void setWidth(Measurement width) {
        this.width = width;
    }

    public void setRayon(Measurement rayon) {
        this.rayon = rayon;
    }

    private void calculatePolygon()
    {
        List<PointMeasurement> arraytemp = new ArrayList<>();
        int nbreEchantillons = (int)(rayon.inches*vertexperinches*Math.PI)/2 + 1;
        PointMeasurement topLeft = new PointMeasurement(topLeftPosition.getX().add(rayon), topLeftPosition.getY().add(rayon));
        Measurement deltax = null;
        Measurement deltay = null;
        for(int i = 0; i<=nbreEchantillons; i++)
        {
            double t = (double)i/nbreEchantillons;
            deltax = rayon.mult(Math.cos(-Math.PI+(Math.PI/2)*t));
            deltay = rayon.mult(Math.sin(-Math.PI+(Math.PI/2)*t));
            arraytemp.add(new PointMeasurement(topLeft.getX().add(deltax), topLeft.getY().add(deltay)));
        }
        PointMeasurement topRight = new PointMeasurement(topLeftPosition.getX().add(width).sub(rayon), topLeftPosition.getY().add(rayon));
        for(int i = 0; i<=nbreEchantillons; i++)
        {
            double t = (double)i/nbreEchantillons;
            deltax = rayon.mult(Math.cos((-Math.PI/2)+(Math.PI/2)*t));
            deltay = rayon.mult(Math.sin((-Math.PI/2)+(Math.PI/2)*t));
            arraytemp.add(new PointMeasurement(topRight.getX().add(deltax), topLeft.getY().add(deltay)));
        }
        PointMeasurement BottomRight = new PointMeasurement(topLeftPosition.getX().add(width).sub(rayon), topLeftPosition.getY().add(height).sub(rayon));
        for(int i = 0; i<=nbreEchantillons; i++)
        {
            double t = (double)i/nbreEchantillons;
            deltax = rayon.mult(Math.cos((Math.PI/2)*t));
            deltay = rayon.mult(Math.sin((Math.PI/2)*t));
            arraytemp.add(new PointMeasurement(BottomRight.getX().add(deltax), BottomRight.getY().add(deltay)));
        }
        PointMeasurement BottomLeft = new PointMeasurement(topLeftPosition.getX().add(rayon), topLeftPosition.getY().add(height).sub(rayon));
        for(int i = 0; i<=nbreEchantillons; i++)
        {
            double t = (double)i/nbreEchantillons;
            deltax = rayon.mult(Math.cos((Math.PI/2)+(Math.PI/2)*t));
            deltay = rayon.mult(Math.sin((Math.PI/2)+(Math.PI/2)*t));
            arraytemp.add(new PointMeasurement(BottomLeft.getX().add(deltax), BottomLeft.getY().add(deltay)));
        }
        arraytemp.add(arraytemp.get(0));
        polygon = new CustomPolygon(arraytemp);
    }
    @Override
    public CustomPolygon getPolygon() {
        if(polygon == null) calculatePolygon();
        return polygon;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        this.vertexperinches = verticesPerInch; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void invalidatePolygon() {
        //points = new ArrayList<>();
        polygon = null; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displaymode = displayMode; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getVertexCount() {
        return vertexperinches;//To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public PointMeasurement getTopLeftPosition() {
        return topLeftPosition;
    }

    public void setTopLeftPosition(PointMeasurement point){
        this.topLeftPosition = point;
    }

    @Override
    public void moove(PointMeasurement point) {
        if(this.moovable){
            this.topLeftPosition = point;
        }
    }

    public Measurement getHeight(){
        return this.height;
    }
    
    public Measurement getWidth(){
        return this.width;
    }
    
    public Measurement getRadius(){
        return this.rayon;
    }
    
}
