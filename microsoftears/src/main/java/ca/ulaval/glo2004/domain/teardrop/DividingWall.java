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
 *l
 * @author Duroy
 */
public class DividingWall implements IDrawable, Cloneable, Serializable {
    
    private TearDropTrailer teardropTrailer;
    private Measurement distanceBeam ;
    private Measurement thickness ;
    private Measurement distanceFloor ;
    private CustomPolygon Polygon;
    private int vertexperinches;
    private DisplayMode displaymode;
    private boolean moovable = true;

    public Measurement getDistanceBeam() {
        return distanceBeam;
    }

    public void setDistanceBeam(Measurement distanceBeam) {
        this.distanceBeam = distanceBeam;
    }

    public Measurement getThickness() {
        return thickness;
    }

    public void setThickness(Measurement thickness) {
        this.thickness = thickness;
    }

    public Measurement getDistanceFloor() {
        return distanceFloor;
    }

    public void setDistanceFloor(Measurement distanceFloor) {
        this.distanceFloor = distanceFloor;
    }

    public DividingWall(TearDropTrailer teardropTrailer) {
        this.teardropTrailer = teardropTrailer;
        distanceBeam = new Measurement(10,1);
        thickness = new Measurement(2,1);
        distanceFloor = new Measurement(2,1);
        this.calculatePolygon();
        this.displaymode = DisplayMode.REGULAR;
    }

    public DividingWall(TearDropTrailer teardropTrailer, Measurement distanceBeam, Measurement thickness, Measurement distanceFloor) {
        this.teardropTrailer = teardropTrailer;
        this.distanceBeam = distanceBeam;
        this.thickness = thickness;
        this.distanceFloor = distanceFloor;
    }
    
    private void calculatePolygon()
    {
        PointMeasurement beam_point = teardropTrailer.getBeam().getPolygon().getVertices().get(1);// coin superieur droit du beam
        List<PointMeasurement> table  = teardropTrailer.getCurvedProfile().getPolygon().getVertices();
        Measurement height = (teardropTrailer.getRawProfile().getPolygon().getVertices().get(2).getY().sub(distanceFloor)).sub(teardropTrailer.getFloor().getThickness());
        PointMeasurement p = null;
        PointMeasurement p1 = null;
        PointMeasurement p2 = null;
        PointMeasurement p3 = null;
        PointMeasurement pi = null;
        Measurement constante = height.div(thickness);
        for(int i= 0; i<table.size(); i++)
        {
            if(((table.get(i).sub(beam_point)).norm().compareTo(distanceBeam) == 1) && (table.get(i).getX().compareTo(beam_point.getX())==1))
            {
                p = table.get(i);
                break;
            }
        }
        if(p == null) throw new UserException("Le mur separateur ne peut pas se placer ici");
        for(int i= 0; i<table.size(); i++)
        {
            if(((table.get(i).getX().sub(p.getX())).compareTo(thickness) == 1) && (table.get(i).getX().compareTo(p.getX())==1))
            {  
                p1 = table.get(i);
                break;
            }
        }
        if(p1 == null) throw new UserException("Le mur separateur ne peut pas se placer ici");
        //pi = p.sub(p1);
        //p2 = new PointMeasurement(p.getX().add(pi.getY().mult(constante)), p.getY().sub(pi.getX().mult(constante)));
        //p3 = new PointMeasurement(p2.getX().sub(pi.getX()), p2.getY().sub(pi.getY()));
        List<PointMeasurement> arraytemp = new ArrayList<>();
        arraytemp.add(p);
        for(int i= 0; i<table.size(); i++)
        {
            if((table.get(i).getX().compareTo(p1.getX()) <0) & (table.get(i).getX().compareTo(p.getX()) >0))
            {
                if((p.getY().compareTo(p1.getY()) > 0) && table.get(i).getY().compareTo(p.getY())<0)
                {
                    arraytemp.add(table.get(i));
                }
                if((p.getY().compareTo(p1.getY()) < 0) && table.get(i).getY().compareTo(p1.getY())<0)
                {
                    arraytemp.add(table.get(i));
                }
            }
        }
        if(p.getY().compareTo(p1.getY()) > 0)
        {
            p2 = new PointMeasurement(p1.getX(), p1.getY().add(height).sub(p1.getY()));
            p3 = new PointMeasurement(p.getX(), (p.getY().add(height).sub(p.getY().sub(p1.getY()))).sub(p1.getY()));
        }
        else
        {
            p3 = new PointMeasurement(p.getX(), p.getY().add(height).sub(p.getY()));
            p2 = new PointMeasurement(p1.getX(), (p1.getY().add(height).sub(p1.getY().sub(p.getY()))).sub(p.getY()));
        }
        arraytemp.add(p1);
        
        arraytemp.add(p2);
        arraytemp.add(p3);
        arraytemp.add(p);
        this.Polygon = new CustomPolygon(arraytemp);
        
        
    }
    @Override
    public DisplayMode getDisplayMode() {
        return displaymode; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CustomPolygon getPolygon() {
        if(Polygon == null) calculatePolygon();
        return Polygon;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        this.vertexperinches = verticesPerInch; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void invalidatePolygon() {
        Polygon = null; //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public void moove(PointMeasurement point) {
        if(this.moovable){
            this.setDistanceBeam(point.x);
        }
    }

}
