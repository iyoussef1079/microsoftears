package ca.ulaval.glo2004.domain.teardrop;

import static ca.ulaval.glo2004.domain.teardrop.GeometricUtils.getSegmentsIntersection;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class CustomPolygon implements Cloneable, Serializable {
    protected List<PointMeasurement> vertices;
    
    public CustomPolygon() {
        this.vertices = new ArrayList<>();
    }
    public CustomPolygon(List<PointMeasurement> vertices) {
        this.vertices = vertices;
    }
    
    public List<PointMeasurement> getVertices() {
        return this.vertices;
    }
    
    // Make sure that any points is inside
    public boolean isWithinBonderies(CustomPolygon polygon){
        return this.vertices.parallelStream().anyMatch(vertex -> (polygon.contains(vertex)));
    }
    
    // Make sure that any points is outside
    public boolean isOutsideBonderies(CustomPolygon polygon){
        return this.vertices.parallelStream().anyMatch(vertex -> !(polygon.contains(vertex)));
    }
    
    // One simple way of finding whether the point is inside or outside a simple
    // polygon is to test how many times a ray, starting from the point and going
    // in any fixed direction, intersects the edges of the polygon. If the point
    // is on the outside of the polygon the ray will intersect its edge an even 
    // number of times.
    // See : https://en.wikipedia.org/wiki/Point_in_polygon
    public boolean contains(PointMeasurement point){
        int numberOfTimesTheyIntersect = 0;
        SegmentMeasurement segment = new SegmentMeasurement(
                point,
                new PointMeasurement(
                        new Measurement(9223372036854l),
                        point.y
                )
        );
        List<PointMeasurement> borderVertices = this.getVertices();
        for(int i = 0; i < borderVertices.size() - 1; i++){
            SegmentMeasurement currentSegment = new SegmentMeasurement(borderVertices.get(i), borderVertices.get(i+1));
            if(getSegmentsIntersection(segment, currentSegment) != null){
                numberOfTimesTheyIntersect++;
            }
        }
        return numberOfTimesTheyIntersect%2!=0;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
