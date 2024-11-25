package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ceiling implements IDrawable, Cloneable, Serializable {


    private int vertexPerInches;
    private Measurement thickness;
    private DisplayMode displayMode;
    private CustomPolygon polygon;
    private TearDropTrailer teardroptrailer;

    public Ceiling(TearDropTrailer tearDropTrailer, Measurement thickness) {
        this.thickness = thickness;
        this.teardroptrailer = tearDropTrailer;
        this.calculatePolygon();
    }

    public Ceiling(TearDropTrailer tearDropTrailer) {
        this.teardroptrailer = tearDropTrailer;
        this.thickness = new Measurement(2,1);
        this.displayMode = DisplayMode.REGULAR;
        this.calculatePolygon();
    }

    public Measurement getThickness() {
        return thickness;
    }

    public void setThickness(Measurement thickness) {
        this.thickness = thickness;
    }

    private void calculatePolygon()
    {
        List<PointMeasurement> profile  = teardroptrailer.getCurvedProfile().getPolygon().getVertices();
        //List<PointMeasurement> side  = teardroptrailer.getSideOpening().getPolygon().getVertices();
        List<PointMeasurement> floor = teardroptrailer.getFloor().getPolygon().getVertices();
        List<PointMeasurement> wall = teardroptrailer.getDividingWall().getPolygon().getVertices();
        //Measurement thick = teardroptrailer.getDividingWall().getThickness();
        PointMeasurement startpoint1 = wall.get(wall.size()-4);
        PointMeasurement dummy = wall.get(0);
        Measurement endy = floor.get(1).getY();


        List<PointMeasurement> array1 = new ArrayList<>();
        List<PointMeasurement> arrayt = new ArrayList<>();
        List<PointMeasurement> array2 = new ArrayList<>();

        array1.add(startpoint1);
        for (PointMeasurement p : profile) {
            if ((p.getX().compareTo(startpoint1.getX()) == 1) && p.getY().compareTo(endy) == -1) {
                array1.add(p);
            }
            if ((p.getX().compareTo(dummy.getX()) == 1) && p.getY().compareTo(endy) == -1) {
                arrayt.add(p);
            }
        }
        PointMeasurement startpoint2 = new PointMeasurement(startpoint1.getX(), startpoint1.getY().add(thickness));

        //array2.add(startpoint2);
        int taillmax = arrayt.size();
        for(int i = 0; i< taillmax-2; i++)
        {
            PointMeasurement p = GeometricUtils.getCornerPoint(arrayt.get(i), arrayt.get(i+1), arrayt.get(i+2), thickness);
            if(p.getX().compareTo(startpoint1.getX()) == 1)
            {
                array2.add(p);
            }
        }
        PointMeasurement endpoint1 = array1.get(array1.size()-1);
        PointMeasurement endpoint2 = new PointMeasurement(endpoint1.getX().sub(thickness), endpoint1.getY());
        array2.add(endpoint2);
        /*****************Mesure de test*******************/
        //array2.add(array2.get(0));
        /***************************************************/
        for(int j = array2.size()-1; j>=0 ;j--)
        {
            array1.add(array2.get(j));
        }
        array1.add(startpoint1);
        this.polygon = new CustomPolygon(array1);
    }
    @Override
    public CustomPolygon getPolygon() {
        if(polygon == null) calculatePolygon();
        return polygon;
    }

    @Override
    public void invalidatePolygon() {
        polygon = null;
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        this.vertexPerInches = verticesPerInch;
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getVertexCount() {
        return vertexPerInches;
    }

    @Override
    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void moove(PointMeasurement point) {

    }
}
