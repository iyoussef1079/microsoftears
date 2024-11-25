/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.UserException;
import static com.google.common.truth.Truth.assertThat;

import org.junit.*;
/**
 *
 * @author freud
 */
public class EllipseTest {
    EllipseArc firstEllipse;
    EllipseArc secondEllipse;
    
    @Before
    public void setUp() {
        firstEllipse = new EllipseArc(
                new PointMeasurement(new Measurement(0.0), new Measurement(0.0)),
                new Measurement(1, 1),
                new Measurement(1, 1)
        );
        secondEllipse = new EllipseArc(
                new PointMeasurement(new Measurement(5, 2), new Measurement(1, 3)),
                new Measurement(10, 1),
                new Measurement(5, 1)
        );
    }
    
    @Test(expected = UserException.class)
    public void testCreateInvalidEllipse1() throws UserException {
        Measurement verticalRadius = new Measurement(0.0);
        Measurement horizontalRadius = new Measurement(1.0);
        new EllipseArc(
                new PointMeasurement(new Measurement(0.0), new Measurement(0.0)),
                verticalRadius,
                horizontalRadius
        );
    }
    
    @Test(expected = UserException.class)
    public void testCreateInvalidEllipse2() throws UserException {
        Measurement verticalRadius = new Measurement(1.0);
        Measurement horizontalRadius = new Measurement(-8.0);
        new EllipseArc(
                new PointMeasurement(new Measurement(0.0), new Measurement(0.0)),
                verticalRadius,
                horizontalRadius
        );
    }
    
    @Test
    public void testGetPolygon() {
        CustomPolygon firstPoly = this.firstEllipse.getPolygon();
        assertThat(firstPoly.vertices.size()).isGreaterThan(0);
        
        CustomPolygon secondPoly = this.secondEllipse.getPolygon();
        assertThat(secondPoly.vertices.size()).isGreaterThan(0);
    }
    
    @Test
    public void testSetVerticesPerInch() {
        
        // setup polygon
        this.firstEllipse.setVerticesPerInch(50);
        this.firstEllipse.invalidatePolygon();
        
        int initialVerticesCount = this.firstEllipse.getPolygon().vertices.size();
        
        // update polygon without invalidation
        this.firstEllipse.setVerticesPerInch(100);
        int newVerticesCount = this.firstEllipse.getPolygon().vertices.size();
        
        assertThat(newVerticesCount).isAtLeast(initialVerticesCount * 2 - 2);
        assertThat(newVerticesCount).isAtMost(initialVerticesCount * 2 + 2);
    }
}
