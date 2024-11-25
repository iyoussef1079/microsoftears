/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.UserException;

import java.util.*;
import java.lang.Math;

public class GeometricUtils {
    public static PointMeasurement getCircleIntersection(Map<Measurement, PointMeasurement> guideCircle, SegmentMeasurement segment) {
        // TODO : Find intersections of circle with a segment
        // Equation of a circle: (x - h)² + (y - k)² = r² where (h,k) is the center of the circle.
        Measurement radius = guideCircle.keySet().iterator().next();
        PointMeasurement center = guideCircle.values().iterator().next();
        Measurement centerX = center.x;
        Measurement centerY = center.y;

        // Equation of a line: y = ax + b
        Measurement startPointX = segment.startPoint.x;
        Measurement startPointY = segment.startPoint.y;
        Measurement endPointX = segment.endPoint.x;
        Measurement endPointY = segment.endPoint.y;
        double slope = (double) (endPointY.millimeter - startPointY.millimeter) / (endPointX.millimeter - startPointX.millimeter);
        Measurement bOfSegment = startPointY.sub(startPointX.mult(slope));

        // We will write the result as 0 = ax²+bx+c and we solve it with the general equation
        double a = (double) Math.pow(slope, 2) + 1;
        Measurement b = bOfSegment.mult(2).mult(slope).sub(centerY.mult(2).mult(slope)).sub(centerX.mult(2));
        Measurement c = bOfSegment.power2().add(centerX.power2()).sub(bOfSegment.mult(2).mult(centerY)).add(centerY.power2()).sub(radius.power2());

        // Find discriminant which must be real and positive
        Measurement d = b.power2().sub(c.mult(4).mult(a));
        if (d.millimeter < 0) {
            throw new UserException("Problem with the circle intersection: The roots of the equation are complex.");
        }

        // Find x1 and x2
        Measurement negativeB = b.mult(-1);
        // TODO: PROBLEM with the long in the following line
        // Result is supposed to be 98.79, but we get 112.59 because of the limitation
        Measurement substractDiscriminant = negativeB.sub(d.sqrt());
        Measurement x1 = substractDiscriminant.div(a).div(2);
        Measurement x2 = b.mult(-1).add(d.sqrt()).div(a).div(2);
        if (x1.compareTo(startPointX) == 1 && x1.compareTo(endPointX) == -1) {
            Measurement y1 = x1.mult(slope).add(bOfSegment);
            return new PointMeasurement(x1, y1);
        }
        Measurement y2 = x2.mult(slope).add(bOfSegment);
        return new PointMeasurement(x2, y2);
    }

    public static double[] getAngleRangeFromQuadrant(Quadrant quadrant) {
        double[] angleRange = new double[2];
        switch (quadrant) {
            case BOTTOM_RIGHT:
                angleRange[0] = 0.0;
                angleRange[1] = 0.5 * Math.PI;
                break;
            case BOTTOM_LEFT:
                angleRange[0] = 0.5 * Math.PI;
                angleRange[1] = 1.0 * Math.PI;
                break;
            case TOP_LEFT:
                angleRange[0] = 1.0 * Math.PI;
                angleRange[1] = 1.5 * Math.PI;
                break;
            case TOP_RIGHT:
                angleRange[0] = 1.5 * Math.PI;
                angleRange[1] = 2.0 * Math.PI;
                break;
            default:
                throw new RuntimeException("Invalid quadrant");
        }
        return angleRange;
    }

    public static List<PointMeasurement> sampleSegmentVertices(SegmentMeasurement segment, int verticesPerInch) {
        Measurement segmentLength = segment.getVector().norm();
        int verticesCount = (int) segmentLength.mult(verticesPerInch).getRoundedInches();

        Measurement sampledSegmentLength = segmentLength.div(verticesCount);
        PointMeasurement baseVector = segment.getVector().normalize().mult(sampledSegmentLength);

        List<PointMeasurement> sampledVertices = new ArrayList<>();
        sampledVertices.add(segment.getStartPoint());

        for (int i = 1; i < verticesCount - 1; i++) {
            sampledVertices.add(segment.getStartPoint().add(baseVector.mult(i)));
        }

        sampledVertices.add(segment.getEndPoint());
        return sampledVertices;
    }

    public static SegmentMeasurement getRightParallelSegment(SegmentMeasurement segment, Measurement distance) {
        // Retrieve coordinates of the points
        Measurement x1 = segment.startPoint.x;
        Measurement y1 = segment.startPoint.y;
        Measurement x2 = segment.endPoint.x;
        Measurement y2 = segment.endPoint.y;

        // If the distance is 0, we throw an error
        if (distance.millimeter == (double) 0.0) {
            throw new UserException("Problem with the distance: Distance must not be 0!");
        }

        // Find slope with the equation a = (y2 - y1)/(x2-x1) and perpendicular slope with -a^-1
        double slope = (double) (y2.millimeter - y1.millimeter) / (x2.millimeter - x1.millimeter);
        double perpendicularSlope = (double) (1 / slope) * -1;

        // Find the b of the first and second slope with StartPoint and 
        // EndPoint with the equation b = y - ax
        Measurement b1 = y1.sub(x1.mult(perpendicularSlope));
        Measurement b2 = y2.sub(x2.mult(perpendicularSlope));

        // Find the new x1 and x2 with the equation newX = 
        // x + sqrt(distance^2/(1+1/m^2))
        Measurement newX1 = x1.add(distance.power2().div((1 + Math.pow(perpendicularSlope, -2))).sqrt());
        Measurement newX2 = x2.add(distance.power2().div((1 + Math.pow(perpendicularSlope, -2))).sqrt());

        // Find the new y1 and y2 with the equation y = ax + b
        Measurement newY1 = newX1.mult(perpendicularSlope).add(b1);
        Measurement newY2 = newX2.mult(perpendicularSlope).add(b2);

        // Create the new PointMeasurements and the new SegmentMeasurement
        PointMeasurement newStartPoint = new PointMeasurement(newX1, newY1);
        PointMeasurement newEndPoint = new PointMeasurement(newX2, newY2);
        return new SegmentMeasurement(newStartPoint, newEndPoint);
    }

    public static PointMeasurement getCornerPoint(PointMeasurement startpoint, PointMeasurement midpoint, PointMeasurement endpoint, Measurement distance) {
        double scale = 100d;
        PointMeasurement firstseg = startpoint.sub(midpoint);
        firstseg = new PointMeasurement(firstseg.getX().mult(scale), firstseg.getY().mult(scale));
        PointMeasurement endseg = endpoint.sub(midpoint);
        endseg = new PointMeasurement(endseg.getX().mult(scale), endseg.getY().mult(scale));
        double angle = getAngleBetween(firstseg, endseg);
        double dist = distance.div(Math.sin(angle / 2)).getRoundedInches();
        double d = firstseg.norm().getRoundedInches();
        double ratio = dist/d;
        PointMeasurement dummy = rot(firstseg, angle/2);
        Measurement d1 = dummy.getX().mult(ratio);
        Measurement x = midpoint.getX().sub(d1);
        Measurement y = midpoint.getY().sub(dummy.getY().mult(ratio));

        return  new PointMeasurement(x, y);
    }

    public static double Bernstein(int indice, double variable, int indicemax) // indicemax-> m, indice-> i, variable-> u
    {
        double valeur = binome(indicemax, indice) * Math.pow(variable, (double) indice) * Math.pow((double) (1 - variable), (double) (indicemax - indice));
        return valeur;
    }

    /**
     *
     * @param vectorA
     * @param vectorB
     * @return
     */
    private static double getAngleBetween(PointMeasurement vectorA, PointMeasurement vectorB) {
        //double determ = det(vectorA, vectorB).getRoundedInches();
        double prodscal = dot(vectorA, vectorB).getRoundedInches();
        double norm = vectorA.norm().mult(vectorB.norm()).getRoundedInches();
        return Math.acos(prodscal/norm);
    }

    /**
     * Fonction du calcul du determinant  de deux vecteurs {@link PointMeasurement}
     * @param A premier vecteur dans le sens trigonometrique
     * @param B deuxieme vecteur dans le sens trigonometrique
     * @return retourne le determinant (A.x)*(B.y) - (A.y)*(B.x)
     */
    private static Measurement det(PointMeasurement A, PointMeasurement B) {
        return (A.getX().mult(B.getY())).sub(A.getY().mult(B.getX()));
    }

    /**
     * Fonction du calcul du produit scalaire  de deux vecteurs {@link PointMeasurement}
     * @param A premier vecteur dans le sens trigonometrique
     * @param B deuxieme vecteur dans le sens trigonometrique
     * @return retourne le resultat (A.x)*(B.x) + (A.y)*(B.y)
     */
    private static Measurement dot(PointMeasurement A, PointMeasurement B)
    {
        return (A.getX().mult(B.getX())).add(A.getY().mult(B.getY()));
    }

    /**
     *
     * @param point
     * @param angle
     * @return
     */
    private static PointMeasurement rot(PointMeasurement point, double angle)
    {
        Measurement x_new = new Measurement(0);
        Measurement y_new = new Measurement(0);
        Measurement x = point.getX();
        Measurement y = point.getY();
        x_new = x.mult(Math.cos(angle)).sub(y.mult(Math.sin(angle)));
        y_new = x.mult(Math.sin(angle)).add(y.mult(Math.cos(angle)));
        return new PointMeasurement(x_new, y_new);

    }
    public static int facto(int n)
    {
        //List<Integer> arraytemp = new ArrayList<Integer>();
        if(n == 0) return 1;
        int result = 1;
        for (int i= 1; i<=n; i++)
        {
            //arraytemp.add(i);
            result = result*i;
        }
       
        return result;
    }
    public static int binome(int n, int k)
    {
        return facto(n)/(facto(k)*facto(n-k));
    }
    public static SegmentMeasurement getLeftParallelSegment(SegmentMeasurement segment, Measurement distance){
        // Retrieve coordinates of the points
        Measurement x1 = segment.startPoint.x;
        Measurement y1 = segment.startPoint.y;
        Measurement x2 = segment.endPoint.x;
        Measurement y2 = segment.endPoint.y;
        
        // If the distance is 0, we throw an error
        if(distance.millimeter == (double) 0.0){
            throw new UserException("Problem with the distance: Distance must not be 0!");
        }
        
        // Find slope with the equation a = (y2 - y1)/(x2-x1) and perpendicular slope with -a^-1
        double slope = (double) (y2.millimeter - y1.millimeter)/(x2.millimeter - x1.millimeter);
        double perpendicularSlope = (double) (1/slope)*-1;
        
        // Find the b of the first and second slope with StartPoint and 
        // EndPoint with the equation b = y - ax
        Measurement b1 = y1.sub(x1.mult(perpendicularSlope));
        Measurement b2 = y2.sub(x2.mult(perpendicularSlope));

        // Find the new x1 and x2 with the equation newX = 
        // x + sqrt(distance^2/(1+1/m^2))
        Measurement newX1 = x1.sub(distance.power2().div((1 + Math.pow(perpendicularSlope, -2))).sqrt());
        Measurement newX2 = x2.sub(distance.power2().div((1 + Math.pow(perpendicularSlope, -2))).sqrt());
        
        // Find the new y1 and y2 with the equation y = ax + b
        Measurement newY1 = newX1.mult(perpendicularSlope).add(b1);
        Measurement newY2 = newX2.mult(perpendicularSlope).add(b2);
        
        // Create the new PointMeasurements and the new SegmentMeasurement
        PointMeasurement newStartPoint = new PointMeasurement(newX1, newY1);
        PointMeasurement newEndPoint = new PointMeasurement(newX2, newY2);
        return new SegmentMeasurement(newStartPoint, newEndPoint);
    }
    
    public static Measurement getDistance(PointMeasurement firstPoint, PointMeasurement secondPoint){
        return secondPoint.x.sub(firstPoint.x).power2().add(secondPoint.y.sub(firstPoint.y).power2()).sqrt();
    }
    
    public static PointMeasurement getClosestSegmentIntersectionInFront(SegmentMeasurement baseSegment, List<SegmentMeasurement> candidateSegments){
        SortedMap<Measurement, PointMeasurement> sortedSegments = new TreeMap();
        candidateSegments.stream().map(segment -> getSegmentsIntersection(baseSegment, segment)).filter(intersection -> (intersection != null)).forEachOrdered(intersection -> {
            Measurement vectorDistance = baseSegment.startPoint.x.sub(intersection.x).power2()
                    .add(baseSegment.startPoint.y.sub(intersection.y).power2()).sqrt();
            if (vectorDistance.millimeter >= 0) {
                // The intersection is in the correct direction
                sortedSegments.put(vectorDistance, intersection);
            }
        });
        if(sortedSegments.isEmpty()){
            return null;
        }
        return sortedSegments.values().iterator().next();
    }
    
    public static PointMeasurement getSegmentsIntersection(CustomPolygon customPolygon, SegmentMeasurement segment){
        for (int i = 0; i < customPolygon.getVertices().size() - 1; i++) {
            PointMeasurement intersection = getSegmentsIntersection(new SegmentMeasurement(customPolygon.getVertices().get(i), customPolygon.getVertices().get(i+1)), segment);
            if (intersection != null) {
                return intersection;
            }
        }
        return null;
    }
    
    public static PointMeasurement getSegmentsIntersection(SegmentMeasurement baseSegment, SegmentMeasurement segment){
        // Adapted algo from: https://www.swtestacademy.com/intersection-convex-polygons-algorithm/

        Measurement A1 = baseSegment.getEndPoint().getY().sub(baseSegment.getStartPoint().getY());
        Measurement B1 = baseSegment.getStartPoint().getX().sub(baseSegment.getEndPoint().getX());
        Measurement C1 = A1.mult(baseSegment.getStartPoint().getX()).add(B1.mult(baseSegment.getStartPoint().getY()));


        Measurement A2 = segment.getEndPoint().getY().sub(segment.getStartPoint().getY());
        Measurement B2 = segment.getStartPoint().getX().sub(segment.getEndPoint().getX());
        Measurement C2 = A2.mult(segment.getStartPoint().getX()).add(B2.mult(segment.getStartPoint().getY()));

        Measurement determinant = A1.mult(B2).sub(A2.mult(B1));
        if (determinant.equals(Measurement.zero())) {
            // TODO: check which vertex to return
            throw new UserException("Segments are parallel");
        } else {
            Measurement x = (B2.mult(C1).sub(B1.mult(C2))).div(determinant);
            Measurement y = (A1.mult(C2).sub(A2.mult(C1))).div(determinant);
            PointMeasurement point = new PointMeasurement(x, y);
            if (isPointWithinRectangle(baseSegment, point) && isPointWithinRectangle(segment, point)) {
                return point;
            } else {
                return null;
            }
        }
    }

    public static boolean isPointWithinRectangle(SegmentMeasurement rectangle, PointMeasurement point) {
        boolean withinX = (rectangle.getStartPoint().getX().compareTo(point.getX()) >= 0
                && rectangle.getEndPoint().getX().compareTo(point.getX()) <= 0)
                || (rectangle.getStartPoint().getX().compareTo(point.getX()) <= 0
                && rectangle.getEndPoint().getX().compareTo(point.getX()) >= 0);
        boolean withinY = (rectangle.getStartPoint().getY().compareTo(point.getY()) >= 0
                && rectangle.getEndPoint().getY().compareTo(point.getY()) <= 0)
                || (rectangle.getStartPoint().getY().compareTo(point.getY()) <= 0
                && rectangle.getEndPoint().getY().compareTo(point.getY()) >= 0);
        return withinX && withinY;
    }
}