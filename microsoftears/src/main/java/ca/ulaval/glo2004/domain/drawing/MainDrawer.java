/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.drawing;

import ca.ulaval.glo2004.domain.controller.Controller;
import ca.ulaval.glo2004.domain.controller.DisplayMode;
import ca.ulaval.glo2004.domain.teardrop.IDrawable;
import ca.ulaval.glo2004.domain.teardrop.PointMeasurement;
import ca.ulaval.glo2004.domain.teardrop.CustomPolygon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.Polygon;
import java.util.Iterator;

/**
 *
 * @author Utilisateur
 */
public class MainDrawer {

    private Controller controller;

    public MainDrawer(Controller controller, Dimension initialDimension) {
        this.controller = controller;
    }

    public void draw(Graphics g) {
        if(this.controller.getTearDrop() != null){
            drawPolygon(g);
        }
        
    }


    //Méthode pour dessiner les polygones de la micro-roulotte
    private void drawPolygon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        ArrayList<IDrawable> listeOfPol = this.controller.getTearDrop().getDrawables();
        CustomPolygon polygon;
        float alpha;
        Color color;
        for (IDrawable iDrawable : listeOfPol) {
            if (iDrawable.getDisplayMode() == DisplayMode.REGULAR) {
                polygon = iDrawable.getPolygon();
                alpha = 0.55f;
                color = new Color(0, 0, 0, alpha); // Black
                g2.setPaint(color);
                g2.setStroke(new BasicStroke(1.5f));
                drawPolyline(g, polygon);
            } else if (iDrawable.getDisplayMode() == DisplayMode.HIGH_LIGHT) {
                polygon = iDrawable.getPolygon();
                alpha = 0.95f;
                color = new Color(0.5f, 0.5f, 0, alpha); // Olive haha
                g2.setPaint(color);
                g2.setStroke(new BasicStroke(3));
                drawPolyline(g, polygon);
            }
        }
    }

    //Méthode pour relier chaque point d'un Custompolygon
    private void drawPolyline(Graphics g, CustomPolygon polygon) {

        ArrayList<PointMeasurement> listeOfPointMeasurement = (ArrayList<PointMeasurement>) polygon.getVertices();
        ArrayList<Point> listOfPoint = new ArrayList<Point>();

        //On créé une nouvelle liste de point qui contiendra les points convertis en pixel
        for (int i = 0; i < listeOfPointMeasurement.size(); i++) {
            Point2D.Double p = this.controller.unitToDecimalPixel(listeOfPointMeasurement.get(i));
            Point pConnvertis = this.controller.absoluteToRelative(p);
            listOfPoint.add(pConnvertis);
        }

        for (int i = 0; i < listOfPoint.size() - 1; i++) {
            g.drawLine(listOfPoint.get(i).x, listOfPoint.get(i).y, listOfPoint.get(i+1).x, listOfPoint.get(i+1).y);
        }
    }
}
