/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.teardrop;

import static com.google.common.truth.Truth.assertThat;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;


public class CustomPolygonTest {
    CustomPolygon polygonInside;
    List<PointMeasurement> polygonInsideVertices;
    CustomPolygon polygonOutside;
    List<PointMeasurement> polygonOutsideVertices;
    CustomPolygon polygonInsideAndOutside;
    List<PointMeasurement> polygonInsideAndOutsideVertices;
    CustomPolygon polygonSharingBonderies;
    List<PointMeasurement> polygonSharingBonderiesVertices;
    PointMeasurement randomPoint;
    boolean result;
    
    @Before
    public void setUp() {
        polygonInsideVertices = new ArrayList<PointMeasurement>() {
            {
                add(new PointMeasurement(new Measurement(2l), new Measurement(2l)));
                add(new PointMeasurement(new Measurement(2l), new Measurement(4l)));
                add(new PointMeasurement(new Measurement(4l), new Measurement(4l)));
                add(new PointMeasurement(new Measurement(4l), new Measurement(2l)));
            }
        };
        polygonInside = new CustomPolygon(polygonInsideVertices);
        
        polygonOutsideVertices = new ArrayList<PointMeasurement>() {
            {
                add(new PointMeasurement(new Measurement(1l), new Measurement(1l)));
                add(new PointMeasurement(new Measurement(1l), new Measurement(5l)));
                add(new PointMeasurement(new Measurement(5l), new Measurement(5l)));
                add(new PointMeasurement(new Measurement(5l), new Measurement(1l)));
            }
        };
        polygonOutside = new CustomPolygon(polygonOutsideVertices);
        
        polygonInsideAndOutsideVertices = new ArrayList<PointMeasurement>() {
            {
                add(new PointMeasurement(new Measurement(3l), new Measurement(3l)));
                add(new PointMeasurement(new Measurement(3l), new Measurement(6l)));
                add(new PointMeasurement(new Measurement(6l), new Measurement(6l)));
                add(new PointMeasurement(new Measurement(6l), new Measurement(3l)));
            }
        };
        polygonInsideAndOutside = new CustomPolygon(polygonInsideAndOutsideVertices);
        
        polygonSharingBonderiesVertices = new ArrayList<PointMeasurement>() {
            {
                add(new PointMeasurement(new Measurement(2l), new Measurement(2l)));
                add(new PointMeasurement(new Measurement(2l), new Measurement(6l)));
                add(new PointMeasurement(new Measurement(6l), new Measurement(6l)));
                add(new PointMeasurement(new Measurement(3l), new Measurement(2l)));
            }
        };
        polygonSharingBonderies = new CustomPolygon(polygonSharingBonderiesVertices);
    }
    
    // First test is with a polygon completely inside the other
    @Test
    public void testIsWithinBonderies() {
        result = polygonInside.isWithinBonderies(polygonOutside);
        assertThat(result).isTrue();
    }
    
    // Second test is with a polygon completely inside the other
    @Test
    public void testIsSharingBounderies() {
        result = polygonSharingBonderies.isWithinBonderies(polygonOutside);
        assertThat(result).isTrue();
    }
    
    // Third test is with a polygon completely inside the other
    @Test
    public void testInsideAndOutsideBounderies() {
        result = polygonInsideAndOutside.isWithinBonderies(polygonOutside);
        assertThat(result).isTrue();
    }
    
    @Test
    public void testPointIsOutsideBounderies() {
        randomPoint = new PointMeasurement(new Measurement(1l), new Measurement(2l));
        result = polygonInside.contains(randomPoint);
        assertThat(result).isFalse();
    }
    
    @Test
    public void testPointIsInsideBounderies() {
        randomPoint = new PointMeasurement(new Measurement(3l), new Measurement(3l));
        result = polygonInside.contains(randomPoint);
        assertThat(result).isTrue();
    }
}
