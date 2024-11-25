/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.teardrop;


import ca.ulaval.glo2004.domain.controller.DisplayMode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Utilisateur
 */
public class TearDropTrailer implements Cloneable, Command, Serializable {

    private EllipseArc e;
    private Floor floor;
    private Beam beam;
    private RawProfile rawProfile;
    private CurvedProfile curvedProfile;
    private Hatch hatch;
    private ArrayList<IDrawable> drawables;
    private DividingWall dividingWall;
    private SideOpening door;
    private SideOpening window;
    private SideOpening window1;
    private Strut strut;

    private Map<Quadrant, ControlPoint> ellipsesControlPoints; // ellipse curved profile
    private ArrayList<ControlPoint> controlPoints; // bezier
    public GuideRectangle rectangle1;
    public GuideRectangle rectangle2;

    private Ceiling ceiling;


    public SideOpening getDoor() {
        return door;
    }

    public void setDoor(SideOpening door) {
        this.door = door;
    }

    public SideOpening getWindow() {
        return window;
    }
    public SideOpening getWindow1(){
        return window1;
    }

    public void setWindow(SideOpening window) {
        this.window = window;
    }

    public TearDropTrailer(Boolean profile)
    {
        drawables = new ArrayList<>();
        controlPoints = new ArrayList<>();
        createRawProfile();
        createFloor();
        createProfile(profile);
        createBeam();
        createHatch();
        createDividingWall();
        createdoor();
        createwindow();
        createStrut();
        createCeiling();
        createRectangleGuide();
        
    }


    public DividingWall getDividingWall() {
        return dividingWall;
    }

    public void setDividingWall(DividingWall dividingWall) {
        this.dividingWall = dividingWall;
    }
    
    public void setEllipseArc(EllipseArc ellipse) {
        this.e = ellipse;
    }
    

    public void setVerticesPerInch(int verticesPerInch)
    {
        for(int i= 0; i < drawables.size(); i++)
        {
            drawables.get(i).setVerticesPerInch(verticesPerInch);
        }
    }
    public void createdoor()
    {
        door = new SideOpening(this);
        drawables.add(door);

    }

    public void createwindow()
    {
        Measurement height = new Measurement(10,1);
        Measurement width = new Measurement(25,1);
        Measurement rayon = new Measurement(9,2);
        PointMeasurement topLeftPosition = new PointMeasurement(new Measurement(-15,1),new Measurement(-15,1));
        if(window == null){
            window = new SideOpening(this, height, width, rayon, topLeftPosition);
            drawables.add(window);
        }
        else{
            window1 = new SideOpening(this, height, width, rayon, topLeftPosition);
            drawables.add(window1);
        }
        
    }

    public void setCeiling(Measurement thickness)
    {
        ceiling.setThickness(thickness);
        invalidateAll();
    }
    public void invalidateAll()
    {
        for(int i= 0; i < drawables.size(); i++)
        {
            drawables.get(i).invalidatePolygon();
        }
    }
    public ArrayList<IDrawable> getDrawables()
    {
        return drawables;
    }
    
    public void createFloor()
    {
        this.floor = new Floor(this);
        drawables.add(this.floor);
    }

    public void createCeiling()
    {
        this.ceiling = new Ceiling(this);
        drawables.add(this.ceiling);
    }
    
    public Floor getFloor(){
        return this.floor;
    }
    
    public void createDividingWall()
    {
        this.dividingWall = new DividingWall(this);
        drawables.add(this.dividingWall);
    }
    

    public void setFloor(Measurement thickness, Measurement frontmargin, Measurement backmargin)
    {
        floor.setBackmargin(backmargin);
        floor.setFrontmargin(frontmargin);
        floor.setThickness(thickness);
        this.invalidateAll();
    }
    
    public void setFloor(Floor floor)
    {
        this.floor = floor;
    }
    
    public void setBeam(Measurement height, Measurement width, Measurement topLeftPositionX)
    {
        beam.setHeight(height);
        beam.setTopLeftPositionX(topLeftPositionX);
        beam.setWidth(width);
        this.invalidateAll();
    }
    
    public void setBeam(Beam beam)
    {
        this.beam = beam;
    }
    
    public void createBeam()
    {
        this.beam = new Beam(this);
        drawables.add(this.beam);
    }
    public Beam getBeam(){
        return this.beam;
    }
    
    public EllipseArc getEllipseArc(){
        return this.e;
    }
    
    public void setSideWall(Measurement height, Measurement width)
    {
        this.rawProfile.setHeight(height);
        this.rawProfile.setWidth(width);
        this.invalidateAll();
    }
    public void setDoor(PointMeasurement topLeftPosition, Measurement height, Measurement width)
    {
        this.door.setTopLeftPosition(topLeftPosition);
    }
    public void setEllispe(Quadrant quadrant, Measurement verticalRadius, Measurement horizontalRadius){
        EllipseCurvedProfile profile = (EllipseCurvedProfile) this.curvedProfile;
        profile.setEllipse(quadrant, verticalRadius, horizontalRadius);
        this.invalidateAll();
    }
    

    public RawProfile getRawProfile() {
        return this.rawProfile;
    }

    public CurvedProfile getCurvedProfile() {
        return this.curvedProfile;
    }

    public void createRawProfile()
    {
        this.rawProfile = new RawProfile(this);
        this.drawables.add(this.rawProfile);
    }

    private void createEllipseCurvedProfile()
    {
        // Create default ellipses center control points
        this.ellipsesControlPoints = new EnumMap<>(Quadrant.class);
        Map<Quadrant, PointMeasurement> shiftsMap = new EnumMap<>(Quadrant.class);
        shiftsMap.put(Quadrant.TOP_LEFT, new PointMeasurement(new Measurement(4, Unit.INCHES), new Measurement(4, Unit.INCHES)));
        shiftsMap.put(Quadrant.TOP_RIGHT, new PointMeasurement(new Measurement(4, Unit.INCHES).mult(-1), new Measurement(4, Unit.INCHES)));
        shiftsMap.put(Quadrant.BOTTOM_LEFT, new PointMeasurement(new Measurement(3.5, Unit.INCHES), new Measurement(3.5, Unit.INCHES).mult(-1)));
        shiftsMap.put(Quadrant.BOTTOM_RIGHT, new PointMeasurement(new Measurement(4, Unit.INCHES).mult(-1), new Measurement(4, Unit.INCHES).mult(-1)));

        for (Quadrant quadrant : Quadrant.values()) {
            PointMeasurement sideWallCorner = this.getRawProfile().getCornerPosition(quadrant);
            ControlPoint controlPoint = new ControlPoint(sideWallCorner.add(shiftsMap.get(quadrant)), this);
            this.ellipsesControlPoints.put(quadrant, controlPoint);

        }

        // Create profile
        this.curvedProfile = new EllipseCurvedProfile(this);
        this.drawables.add(this.curvedProfile);

        this.drawables.addAll(ellipsesControlPoints.values());
    }
    
    private void createBezierCurvedProfile(){
        Measurement x1_test = new Measurement(-55,1);
        Measurement y1_test = new Measurement(-30,1);
        Measurement x2_test = new Measurement(65,1);
        Measurement y2_test = new Measurement(-50,1);
        PointMeasurement p1_test = new PointMeasurement(x1_test, y1_test);
        PointMeasurement p2_test = new PointMeasurement(x2_test, y2_test);
        ControlPoint p = new ControlPoint(p1_test, this);
        ControlPoint p2 = new ControlPoint(p2_test, this);
        this.controlPoints.add(p);
        this.controlPoints.add(p2);
        this.curvedProfile = new BezierCurvedProfile(this);
        this.drawables.add(this.curvedProfile);
        this.drawables.addAll(controlPoints);
    }
    
    public void createProfile(Boolean ellipseProfile){
        if(ellipseProfile){
            this.createEllipseCurvedProfile();
        }
        else{
            this.createBezierCurvedProfile();
        }
    }
    
    public Hatch getHatch(){
        return this.hatch;
    }
    
    public void setHacth(Measurement thickness, Measurement distanceBeam, Measurement distanceFloor, Measurement curveRadius)
    {
        hatch.setDistanceBeam(distanceBeam);
        hatch.setDistanceFloor(distanceFloor);
        hatch.setThickness(thickness);
        hatch.setCurveRadius(curveRadius);
        this.invalidateAll();
    }
    
    public void createHatch()
    {
        this.hatch = new Hatch(this);
        drawables.add(this.hatch);
    }
    

    public void setEllipseCenter(Quadrant quadrant, PointMeasurement center) {
        this.ellipsesControlPoints.get(quadrant).setCenter(center);
        this.invalidateAll();
        
    }
    
    public void setRawProfile (RawProfile rawProfile){
        this.rawProfile = rawProfile;
    }
    
    public void setCurvedProfile (CurvedProfile curvedProfile){
        this.curvedProfile = curvedProfile;
    }
    
    public void setHatch (Hatch hatch){
        this.hatch = hatch;
    }

    public void createStrut() {
        this.strut = new Strut(this);
        this.drawables.add(this.strut);
    }

    public void setDrawables (ArrayList<IDrawable> drawables){
        this.drawables = drawables;
    }
    
    public void setStrut (Strut strut){
        this.strut = strut;
    }
    
    public void setCeiling (Ceiling ceiling){
        this.ceiling = ceiling;
    }
    
    public void setControlPoints (ArrayList<ControlPoint> controlPoints){
        this.controlPoints = controlPoints;
    }
    
    public ArrayList<ControlPoint> getControlPoints (ArrayList<ControlPoint> controlPoints){
        return this.controlPoints;
    }
    
    public void setEllipsesControlPoints (Map<Quadrant, ControlPoint> ellipsesControlPoints){
        this.ellipsesControlPoints = ellipsesControlPoints;
    }
    
    public Map<Quadrant, ControlPoint> getEllipsesControlPoints (Map<Quadrant, ControlPoint> ellipsesControlPoints){
        return this.ellipsesControlPoints;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        // We implemented the alternative to Cloneable : https://www.infoworld.com/article/2077578/java-tip-76--an-alternative-to-the-deep-copy-technique.html
        ObjectOutputStream newObjectOutputStream = null;
        ObjectInputStream newObjectInputStream = null;
        try
        {
           ByteArrayOutputStream newByteArrayOutputStream = new ByteArrayOutputStream();
           newObjectOutputStream = new ObjectOutputStream(newByteArrayOutputStream);
           newObjectOutputStream.writeObject(this);
           newObjectOutputStream.flush();
           ByteArrayInputStream newByteArrayInputStream = new ByteArrayInputStream(newByteArrayOutputStream.toByteArray());
           newObjectInputStream = new ObjectInputStream(newByteArrayInputStream);
           return newObjectInputStream.readObject();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public TearDropTrailer undo() {
        System.out.println(this + " undone");
        return this;
    }

    @Override
    public TearDropTrailer redo() {
        System.out.println(this + " redone");
        return this;
    }

    public Strut getStrut() {
        return this.strut;
    }

    public void setDividingWall(Measurement dBeam, Measurement dFloor, Measurement thickness) {
        dividingWall.setDistanceBeam(dBeam);
        dividingWall.setDistanceFloor(dFloor);
        dividingWall.setThickness(thickness);
        this.invalidateAll();
    }

    public ArrayList<ControlPoint> getControlPoints() {
        return this.controlPoints;
    }

    public Map<Quadrant, ControlPoint> getEllipsesControlPoints() {
        return this.ellipsesControlPoints;
    }
    
    public void addControlPoint(PointMeasurement point){ // ajout des points à la création du profil
        ControlPoint control = new ControlPoint(point, this);
        this.controlPoints.add(control);
        this.invalidateAll();
    }
    
    public void addControlPoint(){ // ajout poste création nombre aléatoire
        if(BezierCurvedProfile.class.isInstance(curvedProfile)){
            int size = controlPoints.size();
            Measurement x = new Measurement(controlPoints.get(size - 1).getCenter().x.inches);
            x.add(new Measurement(10, 1));
            Measurement y = new Measurement(controlPoints.get(size - 1).getCenter().y.inches);
            PointMeasurement center = new PointMeasurement(x, y);
            ControlPoint control = new ControlPoint(center, this);
            this.controlPoints.add(control);
            this.drawables.add(control);
            this.invalidateAll();
        }
    }
    
    public Ceiling getCeiling(){
        return this.ceiling;
    }

    public void setPoidsHatch(float poids) {
        hatch.setDeadWeightPounds(poids);
        invalidateAll();
    }

    public void deleteComponant(IDrawable i) {
        try{
            drawables.remove(i);
            controlPoints.remove(i);
        }catch(Exception e){
            
        }
        invalidateAll();
    }

    public void createRectangleGuide() {
        rectangle1 = new GuideRectangle(this);
        rectangle2 = new GuideRectangle(this);
        rectangle1.setDisplayMode(DisplayMode.HIDDEN);
        rectangle2.setDisplayMode(DisplayMode.HIDDEN);
        rectangle2.setHeight(new Measurement(72, 1));
        rectangle2.setWidth(new Measurement(10, 1));
        rectangle2.setTopLeftPosition(new PointMeasurement(new Measurement(60,1), new Measurement(-50,1)));
        drawables.add(rectangle1);
        drawables.add(rectangle2);
    }
}

