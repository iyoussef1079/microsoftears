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
public class BezierCurvedProfile extends CurvedProfile implements Serializable{
    List<PointMeasurement> controlPoints;
    protected CustomPolygon polygon;
    private int vertexperinches;
    private DisplayMode displaymode;
    private TearDropTrailer teardroptrailer;
    private boolean moovable = false;

    public BezierCurvedProfile(List<PointMeasurement> controlPoints, TearDropTrailer teardroptrailer) {
        this.controlPoints = controlPoints;
        this.teardroptrailer = teardroptrailer;
    }

    public BezierCurvedProfile(TearDropTrailer teardroptrailer) {
        this.teardroptrailer = teardroptrailer;
        controlPoints = new ArrayList<>();
        List<PointMeasurement> table  = teardroptrailer.getRawProfile().getPolygon().getVertices();

        
        /***valeur de test . À retirer apres ****/
        //Measurement x1_test = new Measurement(-55,1);
        //Measurement y1_test = new Measurement(-60,1);
        //Measurement x2_test = new Measurement(65,1);
        //Measurement y2_test = new Measurement(-65,1);
        //PointMeasurement p1_test = new PointMeasurement(x1_test, y1_test);
        //PointMeasurement p2_test = new PointMeasurement(x2_test, y2_test);

        /***valeur de test . À retirer apres ****/
        
                
        //controlPoints.add(table.get(3));
        for(ControlPoint c : this.teardroptrailer.getControlPoints()){
            controlPoints.add(c.getCenter());
        }
        //controlPoints.add(p1_test);// normalement table.get(0)
        //controlPoints.add(p2_test);// normalement table.get(1)
        //controlPoints.add(table.get(2));
        vertexperinches = 5;
        this.calculatePolygon();
        this.displaymode = DisplayMode.REGULAR;
    }
    
    private void calculatePolygon()
    {
        List<PointMeasurement> table  = teardroptrailer.getRawProfile().getPolygon().getVertices();
        List<PointMeasurement> table1  = teardroptrailer.getFloor().getPolygon().getVertices();
        List<PointMeasurement> arraytemp = new ArrayList<>();
        List<PointMeasurement> allPoints = new ArrayList<>(); //liste de tous les points y compris les deux points du profil
        /**insertion des points au debut et à la fin**/
        controlPoints.clear();
        for(ControlPoint c : this.teardroptrailer.getControlPoints()){
            controlPoints.add(c.getCenter());
        }
        allPoints.add(table1.get(3));
        allPoints.addAll(controlPoints);
        allPoints.add(table1.get(2));
        /** fin de l'insertion**/
        /// dimensions de protection
        Measurement x1_guard = table.get(3).getX();
        Measurement x2_guard = table.get(2).getX();
        Measurement y1_guard = table.get(3).getY();
        Measurement y2_guard = table.get(0).getY();
        /////////////////////
        int nbrepoints = allPoints.size()-1;
        double bernstein;
        int nbreEchantillons = (int)(table.get(3).sub(table.get(2))).norm().inches*vertexperinches;// nombre d'echantillons de temps à considerer
        for(int i = 0; i<=nbreEchantillons; i++)
        {
            Measurement x = new Measurement(0,1);
            Measurement y = new Measurement(0,1);
            for(int j = 0; j<= nbrepoints ; j++)
            {
                double t = (double)i/(double) nbreEchantillons;
                bernstein = GeometricUtils.Bernstein(j, t, nbrepoints);
                Measurement xj = allPoints.get(j).getX();
                Measurement yj = allPoints.get(j).getY();
                x = x.add(xj.mult(bernstein));
                y = y.add(yj.mult(bernstein));
            }
            //barriere de securité anti debordement
            if(x.compareTo(x1_guard) == -1) x = x1_guard;
            if(x.compareTo(x2_guard) == 1) x = x2_guard;
            if(y.compareTo(y1_guard) == 1) y = y1_guard;
            if(y.compareTo(y2_guard) == -1) y = y2_guard;

            arraytemp.add(new PointMeasurement(x, y));
        }
        PointMeasurement first = arraytemp.get(0);
        PointMeasurement last = arraytemp.get(arraytemp.size()-1);
        Measurement weight = last.getX().sub(first.getX());
        for(int i = 0; i<=nbreEchantillons; i++)
        {
            double b = (double)i/(double) nbreEchantillons;
            arraytemp.add(new PointMeasurement(last.getX().sub(weight.mult(b)), last.getY()));
        }
       this.polygon = new CustomPolygon(arraytemp);
    }
    @Override
    public DisplayMode getDisplayMode() {
        return displaymode;
    }

    @Override
    public CustomPolygon getPolygon() {
        if(polygon == null) calculatePolygon();
        return polygon;
    }

    @Override
    public int getBottomLeftCornerIndex() {
        return 0;
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        this.vertexperinches = verticesPerInch;
    }

    @Override
    public void invalidatePolygon() {
        polygon = null;
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displaymode = displayMode;
    }

    @Override
    public int getVertexCount() {
        return vertexperinches;
    }

    @Override
    public void moove(PointMeasurement point) {
        if(moovable) {
            /// dont do anything
        }
    }

}
