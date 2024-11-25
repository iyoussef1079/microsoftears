/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.controller;
import ca.ulaval.glo2004.domain.teardrop.IDrawable;
import ca.ulaval.glo2004.domain.teardrop.Measurement;
import ca.ulaval.glo2004.domain.teardrop.PointMeasurement;
import ca.ulaval.glo2004.domain.teardrop.Quadrant;
import ca.ulaval.glo2004.domain.teardrop.TearDropTrailer;
import ca.ulaval.glo2004.domain.teardrop.UndoRedoManager;
import ca.ulaval.glo2004.domain.teardrop.Unit;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Utilisateur
 */
public class Controller {

    private TearDropTrailer tearDrop;

    //variables pour la gestion du zoom et du paning
    public float decalageX = -550f;//550f
    public float decalageY = -300f;//300f
    public float sourisStartPanX = 0f;
    public float sourisStartPanY = 0f;
    public float sourisX = 0f;
    public float sourisY = 0f;
    public int typeSouris = -1;
    public double zoom = 1;

    private double verticesPerInch = 50;
    private Measurement gridSpacing;
    private boolean gridVisibility;
    private Unit unit;
    private List<String> errors;

    public void editPoidsHatch(float poids) {
        this.tearDrop.setPoidsHatch(poids);
    }

    enum ComponentType {
        CEILING,
        STRUT,
        BEAM,
        HATCH,
        FLOOR,
        SIDE_WALL,
        DIVIDING_WALL,
        SIDE_OPENING,
        RECTANGLE_AID
    }

    public Controller() {
        //this.tearDrop = new TearDropTrailer(true);
    }
    
    public ArrayList<IDrawable> getComponentList(){
        return this.tearDrop.getDrawables();
    }
    
    public void createTeardrop(Boolean ellipse){
        this.tearDrop = new TearDropTrailer(ellipse);
    }

    public IDrawable getComponentClicked(Point2D.Float point) {
        Point2D.Double relativePoint = relativeToAbsolute(point.x, point.y);
        Measurement xReel = new Measurement((double) (relativePoint.x *25.4/ (3.8))); // TODO patch la sélection d'élément
        Measurement yReel = new Measurement((double) (relativePoint.y *25.4/ (3.8)));
        PointMeasurement realPoint = new PointMeasurement(xReel, yReel);
        ListIterator listIterator = this.getComponentList().listIterator(this.getComponentList().size());
        while (listIterator.hasPrevious()) {
            IDrawable iDrawable = (IDrawable) listIterator.previous();
            if(iDrawable.getPolygon().contains(realPoint) && !iDrawable.getClass().getSimpleName().contains("Profile")){
                return iDrawable;
            }
         }
        if(this.tearDrop.getCurvedProfile().getPolygon().contains(realPoint)){
            return (IDrawable) this.tearDrop.getCurvedProfile();
        } else if (this.tearDrop.getRawProfile().getPolygon().contains(realPoint)) {
            return (IDrawable) this.tearDrop.getRawProfile();
        }
        return null;
        
    }
    
    public void changerUnit(Unit metrique) {
        this.unit = metrique;
    }
    
    public void deleteComponant(IDrawable i){
        this.tearDrop.deleteComponant(i);
        
    }

    public Point2D.Double unitToDecimalPixel(PointMeasurement point) {
        Measurement xReel = point.x;
        Measurement yReel = point.y;
        double xScreen = (xReel.getRoundedInches() * 3.8);
        double yScreen = (yReel.getRoundedInches() * 3.8);

        return new Point2D.Double(xScreen, yScreen);
    }

    public PointMeasurement pixelToUnit(Point point) {
        int xScreen = point.x;
        int yScreen = point.y;
        Measurement xReel = new Measurement((double) (xScreen *25.4/ (3.8))); // TODO patch la sélection d'élément
        Measurement yReel = new Measurement((double) (yScreen *25.4/ (3.8)));

        return new PointMeasurement(xReel, yReel);
    }

    public Point2D.Double relativeToAbsolute(float x, float y) {
        double fWorldX = ((double) x / this.zoom) + this.decalageX;
        double fWorldY = ((double) y / this.zoom) + this.decalageY;
        return new Point2D.Double(fWorldX, fWorldY);
    }

    public Point absoluteToRelative(Point2D.Double point) {
        double worldX = point.x;
        double worldY = point.y;
        int nScreenX = (int) ((worldX - this.decalageX) * this.zoom);
        int nScreenY = (int) ((worldY - this.decalageY) * this.zoom);
        return new Point(nScreenX, nScreenY);
    }

    public void editFloor(Measurement thickness, Measurement frontmargin, Measurement backmargin) {
        tearDrop.setFloor(thickness, frontmargin, backmargin);
        //this.componentList = tearDrop.getDrawables();
    }

    public void editHatch(Measurement thickness, Measurement distanceBeam, Measurement distanceFloor, Measurement curveRadius) {
        tearDrop.setHacth(thickness, distanceBeam, distanceFloor, curveRadius);
        //this.componentList = tearDrop.getDrawables();

    }

    public void editBeam(Measurement height, Measurement width, Measurement topLeftPositionX) {
        tearDrop.setBeam(height, width, topLeftPositionX);
        //this.componentList = tearDrop.getDrawables();
    }

    public void editCeiling(Measurement thickness) {
        tearDrop.setCeiling(thickness);
    }

    public void editDividingWall(Measurement dBeam, Measurement dFloor, Measurement thickness) {
        tearDrop.setDividingWall(dBeam, dFloor, thickness);
    }
    // TODO: Harold create door and window edition
    public void editSideOpening(Measurement height, Measurement width, Measurement radius, IDrawable componant) {
        if(componant.equals(tearDrop.getDoor())){
            
            this.tearDrop.getDoor().setHeight(height);
            this.tearDrop.getDoor().setWidth(width);
            this.tearDrop.getDoor().setRayon(radius);
        }
        else if(componant.equals(tearDrop.getWindow())){
           this.tearDrop.getWindow().setHeight(height);
           this.tearDrop.getWindow().setWidth(width);
           this.tearDrop.getWindow().setRayon(radius);
        }else if(componant.equals(tearDrop.getWindow1())){
            this.tearDrop.getWindow1().setHeight(height);
           this.tearDrop.getWindow1().setWidth(width);
           this.tearDrop.getWindow1().setRayon(radius);
        }
        tearDrop.invalidateAll();
        
    }

    public void editSideWall(Measurement height, Measurement width) {
        this.tearDrop.setSideWall(height, width);
    }

    // TODO: Youssef, find where horizontal/vertical radius are switched
    public void editEllipseProfile(Quadrant quadrant, Measurement verticalRadius, Measurement horizontalRadius){
        this.tearDrop.setEllispe(quadrant, verticalRadius, horizontalRadius);
    }
    public void editEllipseCenter(Quadrant quadrant, PointMeasurement center){
        this.tearDrop.setEllipseCenter(quadrant, center);
    }
    public void editGridVisibility(boolean visibility) {
        this.gridVisibility = visibility;
    }

    public void editGridSpacing(Measurement measure) {
        this.gridSpacing = measure;
    }
    
    public ArrayList<IDrawable> getList() {
        return this.getComponentList();
    }
    
    public TearDropTrailer getTearDrop(){
        return this.tearDrop;
    }
    
    public boolean validInput(float input){
        boolean valid = true;
        if(input <= 0) valid = false;
        return valid;
    }
    
    public ArrayList<String> validateTearDrop(){
        TearDropValidator validator = new TearDropValidator(this.tearDrop);
        return validator.validTearDrop();
    }
    
    public void setTearDrop(TearDropTrailer tearDropTrailer){
        this.tearDrop = tearDropTrailer;
    }
    
    public void moove(IDrawable idrawable, PointMeasurement point){
        idrawable.moove(point);
        this.tearDrop.invalidateAll();
              
    }
    public void addControlPoint(){
        this.tearDrop.addControlPoint();
    }
    
    public void createWindow(){
        this.tearDrop.createwindow();
    }
    
    public void createDoor(){
        this.tearDrop.createdoor();
    }
    
    
}
