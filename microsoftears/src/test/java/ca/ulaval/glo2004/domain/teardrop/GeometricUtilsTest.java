
package ca.ulaval.glo2004.domain.teardrop;

import static ca.ulaval.glo2004.domain.teardrop.GeometricUtils.getCircleIntersection;
import static com.google.common.truth.Truth.assertThat;
import java.util.Collections;
import java.util.Map;

import org.junit.*;

public class GeometricUtilsTest {
    PointMeasurement obtainedPointMeasurement;
    
//    @Test
//    public void testGetCircleIntersection() {
//        SegmentMeasurement segment = new SegmentMeasurement(
//                new PointMeasurement(
//                    new Measurement(0, 1),
//                    new Measurement(18, 1)
//                ),
//                new PointMeasurement(
//                    new Measurement(6, 1),
//                    new Measurement(0, 1)
//                )
//        );
//        Measurement radius = new Measurement(3,1);
//        PointMeasurement center = new PointMeasurement(
//                                    new Measurement(7, 1),
//                                    new Measurement(1, 1)
//                                );
//        Map<Measurement, PointMeasurement> guideCircle = Collections.singletonMap(radius, center);
//        PointMeasurement expectedResult = new PointMeasurement(
//                        new Measurement(4.939767473d), new Measurement(3.180697581d)
//                );
//        obtainedPointMeasurement = getCircleIntersection(guideCircle, segment);
//        assertThat(obtainedPointMeasurement.x.millimeter).isEqualTo(expectedResult.x.millimeter);
//        assertThat(obtainedPointMeasurement.y.millimeter).isEqualTo(expectedResult.y.millimeter);
//    }
    
    @Test
    public void testGetSegmentsIntersection1() {
        SegmentMeasurement firstSegment = new SegmentMeasurement(
                new PointMeasurement(
                    new Measurement(0, 1),
                    new Measurement(0, 1)
                ),
                new PointMeasurement(
                    new Measurement(2, 1),
                    new Measurement(2, 1)
                )
        );
        SegmentMeasurement secondSegment = new SegmentMeasurement(
                new PointMeasurement(
                    new Measurement(0, 1),
                    new Measurement(2, 1)
                ),
                new PointMeasurement(
                    new Measurement(2, 1),
                    new Measurement(0, 1)
                )
        );
        PointMeasurement expectedResult = new PointMeasurement(
                        new Measurement(1, 1), new Measurement(1, 1)
                );
        obtainedPointMeasurement = GeometricUtils.getSegmentsIntersection(firstSegment, secondSegment);
        assertThat(obtainedPointMeasurement.x.millimeter).isEqualTo(expectedResult.x.millimeter);
        assertThat(obtainedPointMeasurement.y.millimeter).isEqualTo(expectedResult.y.millimeter);
    }
    
//    @Test // We test two lines that touch at the end of the segment
//    public void testGetSegmentsIntersection2() {
//        SegmentMeasurement firstSegment = new SegmentMeasurement(
//                new PointMeasurement(
//                    new Measurement(0, 1),
//                    new Measurement(0, 1)
//                ),
//                new PointMeasurement(
//                    new Measurement(7, 1),
//                    new Measurement(7, 1)
//                )
//        );
//        SegmentMeasurement secondSegment = new SegmentMeasurement(
//                new PointMeasurement(
//                    new Measurement(6, 1),
//                    new Measurement(6, 1)
//                ),
//                new PointMeasurement(
//                    new Measurement(9, 1),
//                    new Measurement(9, 1)
//                )
//        );
//        PointMeasurement expectedResult = new PointMeasurement(
//                        new Measurement(7, 1), new Measurement(7, 1)
//                );
//        obtainedPointMeasurement = GeometricUtils.getSegmentsIntersection(firstSegment, secondSegment);
//        assertThat(obtainedPointMeasurement.x.millimeter).isEqualTo(expectedResult.x.millimeter);
//        assertThat(obtainedPointMeasurement.y.millimeter).isEqualTo(expectedResult.y.millimeter);
//    }
    
    @Test
    public void testGetIntersectionWithVerticalLine() {
        SegmentMeasurement firstSegment = new SegmentMeasurement(
                new PointMeasurement(
                    new Measurement(0, 1),
                    new Measurement(1, 1)
                ),
                new PointMeasurement(
                    new Measurement(2, 1),
                    new Measurement(1, 1)
                )
        );
        SegmentMeasurement secondSegment = new SegmentMeasurement(
                new PointMeasurement(
                    new Measurement(1, 1),
                    new Measurement(2, 1)
                ),
                new PointMeasurement(
                    new Measurement(1, 1),
                    new Measurement(0, 1)
                )
        );
        PointMeasurement expectedResult = new PointMeasurement(
                        new Measurement(1, 1), new Measurement(1, 1)
                );
        obtainedPointMeasurement = GeometricUtils.getSegmentsIntersection(firstSegment, secondSegment);
        assertThat(obtainedPointMeasurement.x.millimeter).isEqualTo(expectedResult.x.millimeter);
        assertThat(obtainedPointMeasurement.y.millimeter).isEqualTo(expectedResult.y.millimeter);
    }
}
