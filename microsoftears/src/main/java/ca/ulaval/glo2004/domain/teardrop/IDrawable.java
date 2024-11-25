package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;

public interface IDrawable {
    public boolean moovable = false;
    public static int DEFAULT_VERTICES_PER_INCH = 6;
    
    /**
     * Generates a polygon approximation of the IDrawable.
     * @return The generated polygon.
     */
    public CustomPolygon getPolygon();
    
    /**
     * Method used to invalidate the computed polygon.
     * 
     * It should be called whenever a component is moved on components that
     * depend on it to be drawn.
     */
    // TODO: change name to invalidate()
    public void invalidatePolygon();
    
    /**
     * Sets the number of vertices per inch, which corresponds to the resolution
     * of the polygon.
     * 
     * In some cases (e.g. the Ellipse object), this resolution is not exactly
     * matched however.
     * 
     * @param verticesPerInch The number of vertex per inch (polygon resolution)
     */
    public void setVerticesPerInch(int verticesPerInch);
    
    /**
     * Sets the visibility of the IDrawable.
     * 
     * An invisible IDrawable should have an empty polygon associated to it.
     * @param visible The visibility of the IDrawable.
     */
    public void setDisplayMode(DisplayMode displayMode);
    
    /**
     * The computed number of vertices associated with the polygon that matches
     * the number of vertices per inch.
     * @return The vertex count.
     */
    public int getVertexCount();

    
    public DisplayMode getDisplayMode();


    //public Object clone() throws CloneNotSupportedException;
    
    public void moove(PointMeasurement point);
}
