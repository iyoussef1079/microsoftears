package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import ca.ulaval.glo2004.domain.controller.UserException;
import java.io.Serializable;
import java.util.ArrayList;

public class EllipseArc implements IDrawable, Cloneable, Serializable {
    public static final int DEFAULT_VERTICAL_RADIUS_INCHES = 1;
    public static final int DEFAULT_HORIZONTAL_RADIUS_INCHES = 1;
    
    private PointMeasurement center;
    private Measurement verticalRadius;
    private Measurement horizontalRadius;
    protected double startAngle;
    protected double endAngle;
    protected CustomPolygon polygon;
    protected DisplayMode displayMode;
    protected int verticesPerInch;
    private boolean moovable = false;
    
    public EllipseArc() {
        this(new PointMeasurement(new Measurement(0.0), new Measurement(0)), 
                new Measurement(DEFAULT_VERTICAL_RADIUS_INCHES, 1), 
                new Measurement(DEFAULT_HORIZONTAL_RADIUS_INCHES, 1));
    }
    
    public EllipseArc(PointMeasurement center, 
            Measurement verticalRadius, 
            Measurement horizontalRadius) {        
        this(center, verticalRadius, horizontalRadius,
                0.0, 2 * Math.PI);
    }
    
    public EllipseArc(PointMeasurement center, 
            Measurement verticalRadius, 
            Measurement horizontalRadius,
            double startAngle,
            double endAngle) {
        if (verticalRadius.getRoundedInches() <= 0) {
            throw new UserException("Problem with the ellipse: Vertical radius must be greater than 0!");
        }
        if (horizontalRadius.getRoundedInches() <= 0) {
            throw new UserException("Problem with the ellipse: Horizontal radius must be greater than 0!");
        }
        if (endAngle < startAngle) {
            throw new UserException("Problem with the ellipse: End angle must be greated than start angle!");
        }
        this.center = center;
        this.verticalRadius = verticalRadius;
        this.horizontalRadius = horizontalRadius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        
        this.polygon = null;
        this.displayMode = DisplayMode.REGULAR;
        this.verticesPerInch = DEFAULT_VERTICES_PER_INCH;
    }
    
    @Override
    public CustomPolygon getPolygon() {
        if (this.displayMode == DisplayMode.REGULAR) {
            if (this.polygon == null) {
                ArrayList<PointMeasurement> vertices = new ArrayList<>();
                int vertexCount = this.getVertexCount();
                double angleIncrement = (endAngle - startAngle) / vertexCount;
                
                // parametric form of ellipse
                // x = r1 * cos(2*pi*t)
                // y = r2 * sin(2*pi*t)
                for (int i = 0; i < vertexCount + 1; i++) {
                    double angle = i * angleIncrement + startAngle;
                    Measurement x = center.getX().add(horizontalRadius.mult(Math.cos(angle)));
                    Measurement y = center.getY().add(verticalRadius.mult(Math.sin(angle)));
                    vertices.add(new PointMeasurement(x, y));
                }
                this.polygon = new CustomPolygon(vertices);
            }
            return this.polygon;
        } else {
            return new CustomPolygon();
        }
    }
    
    /*
    public CustomPolygon getVerticesInQuadrant(Quadrant quadrant) {
        List<PointMeasurement> vertices;
        switch (quadrant) {
            case TOP_LEFT:
                vertices = this.getPolygon()
                        .getVertices()
                        .stream()
                        .filter(v -> 
                                v.getX().compareTo(center.getX()) <= 0 &&
                                v.getY().compareTo(center.getY()) <= 0)
                        .collect(Collectors.toList());
                break;
            case TOP_RIGHT:
                vertices = this.getPolygon()
                        .getVertices()
                        .stream()
                        .filter(v -> 
                                v.getX().compareTo(center.getX()) >= 0 &&
                                v.getY().compareTo(center.getY()) <= 0)
                        .collect(Collectors.toList());
                break;
            case BOTTOM_LEFT:          
                vertices = this.getPolygon()
                        .getVertices()
                        .stream()
                        .filter(v -> 
                                v.getX().compareTo(center.getX()) <= 0 &&
                                v.getY().compareTo(center.getY()) >= 0)
                        .collect(Collectors.toList());
                break;
            case BOTTOM_RIGHT:                
                vertices = this.getPolygon()
                        .getVertices()
                        .stream()
                        .filter(v -> 
                                v.getX().compareTo(center.getX()) >= 0 &&
                                v.getY().compareTo(center.getY()) >= 0)
                        .collect(Collectors.toList());
                break;
            default:
                throw new RuntimeException("Invalid quadrant.");
        }
        return new CustomPolygon(vertices);
    }*/
    
    @Override
    public void invalidatePolygon() {
        this.polygon = null;
    }
    
    @Override
    public void setVerticesPerInch(int verticesPerInch) {
        this.verticesPerInch = verticesPerInch;
        this.invalidatePolygon();
    }
    
    @Override
    public int getVertexCount() {
        // estimate vertex count as if we had a rectangle
        // because getting the perimeter of an actual ellipse is analytically impossible
        Measurement ellipsePerimeter = verticalRadius.mult(4).add(horizontalRadius.mult(4));
        double totalAngle = endAngle - startAngle;
        double totalInches = ellipsePerimeter.getRoundedInches() * totalAngle / (2 * Math.PI);
        return (int) Math.ceil(totalInches * verticesPerInch);
    }
    
    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public DisplayMode getDisplayMode() {
         return this.displayMode;
    }
    
    public Measurement getVerticalRadius(){
        return this.verticalRadius;
    }
    
    public Measurement getHorizontalRadius(){
        return this.horizontalRadius;
    }
    
    public PointMeasurement getCenter(){
        return this.center;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void moove(PointMeasurement point) {
        if(this.moovable){
            // dont do anything yet
        }
    }

}
