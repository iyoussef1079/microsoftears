///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package ca.ulaval.glo2004.domain.teardrop;
//
//import ca.ulaval.glo2004.domain.controller.UserException;
//import static com.google.common.truth.Truth.assertThat;
//
//import org.junit.*;
///**
// * @author Justin Hogue
// */
//public class MeasurementTest {
//    private static final long FIRST_INCHES_NUMERATOR = 4;
//    private static final long FIRST_INCHES_DENOMINATOR = 9;
//    private static final long SECOND_INCHES_NUMERATOR = 3;
//    private static final long SECOND_INCHES_DENOMINATOR = 7;
//    Measurement firstMeasurement;
//    Measurement secondMeasurement;
//    Measurement obtainedMeasurement;
//    
//    @Before
//    public void setUp() throws UserException {
//        firstMeasurement = new Measurement(FIRST_INCHES_NUMERATOR, FIRST_INCHES_DENOMINATOR);
//        secondMeasurement = new Measurement(SECOND_INCHES_NUMERATOR, SECOND_INCHES_DENOMINATOR);
//    }
//    
//    @Test(expected = UserException.class)
//    public void testCreateInvalidMeasurement() throws UserException {
//        long inchesNumerator = 4;
//        long inchesDenominator = 0;
//        new Measurement(inchesNumerator, inchesDenominator);
//    }
//    
//    @Test
//    public void testGetRoundedInches() {
//        double result = 0.8;
//        long inchesNumerator = 4;
//        long inchesDenominator = 5;
//        Measurement measurement = new Measurement(inchesNumerator, inchesDenominator);
//        double expectedResult = measurement.getRoundedInches();
//        assertThat(result).isEqualTo(expectedResult);
//    }
//    
//    @Test
//    public void testAdd() {
//        long inchesNumerator = 55; // 4*7 + 3*9
//        long inchesDenominator = 63; // 9*7
//        Measurement addedMeasurement = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.add(secondMeasurement);
//        assertThat(addedMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(addedMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test
//    public void testSub() {
//        long inchesNumerator = 1; // 4*7 - 3*9
//        long inchesDenominator = 63; // 9*7
//        Measurement subbedMeasurement = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.sub(secondMeasurement);
//        assertThat(subbedMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(subbedMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test
//    public void testMultWithMeasurement() {
//        long inchesNumerator = 4; // 4*3/3
//        long inchesDenominator = 21; // 9*7/3
//        Measurement multipliedMeasurement = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.mult(secondMeasurement);
//        assertThat(multipliedMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(multipliedMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test
//    public void testMultWithDouble() {
//        // 4/9 * 0.123456789
//        long inchesNumerator = 13717421; // 4*123456789/36
//        long inchesDenominator = 250000000L; // 9*1000000000/36
//        Measurement multipliedMeasurement = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.mult(0.123456789);
//        assertThat(multipliedMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(multipliedMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test
//    public void testMultWithInt() {
//        long inchesNumerator = 4; // 4*3/3
//        long inchesDenominator = 3; // 9/3
//        Measurement multipliedMeasurement = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.mult(3);
//        assertThat(multipliedMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(multipliedMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test(expected = UserException.class)
//    public void testInvalidDiv() {
//        long inchesNumerator = 0;
//        long inchesDenominator = 5;
//        Measurement measurement = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.div(measurement);
//    }
//    
//    @Test
//    public void testValidDiv() {
//        long inchesNumerator = 28; // 4*7
//        long inchesDenominator = 27; // 3*9
//        Measurement dividedMeasurement = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.div(secondMeasurement);
//        assertThat(dividedMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(dividedMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test
//    public void testPower2() {
//        long inchesNumerator = 16; // 4^2
//        long inchesDenominator = 81; // 9^2
//        Measurement measurementExponent2 = new Measurement(inchesNumerator, inchesDenominator);
//        obtainedMeasurement = firstMeasurement.power2();
//        assertThat(measurementExponent2.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(measurementExponent2.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test
//    public void testSqrt() {
//        double sqrtRootMillimiter = Math.sqrt(firstMeasurement.millimeter);
//        Measurement squaredMeasurement = new Measurement(sqrtRootMillimiter);
//        obtainedMeasurement = firstMeasurement.sqrt();
//        assertThat(squaredMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(squaredMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
//    @Test
//    public void testCompareTo() {
//        int isLessThan = -1;
//        int isBiggerThan = 1;
//        assertThat(isBiggerThan).isEqualTo(firstMeasurement.compareTo(secondMeasurement));
//        assertThat(isLessThan).isEqualTo(secondMeasurement.compareTo(firstMeasurement));
//    }
//    
//    @Test
//    public void testEquals() {
//        obtainedMeasurement =  new Measurement(FIRST_INCHES_NUMERATOR, FIRST_INCHES_DENOMINATOR);
//        assertThat(firstMeasurement.inchesNumerator).isEqualTo(obtainedMeasurement.inchesNumerator);
//        assertThat(firstMeasurement.inchesDenominator).isEqualTo(obtainedMeasurement.inchesDenominator);
//    }
//    
////    @Test
////    public void testToString() {
////        Measurement measurement;
////        long inchesNumerator = 4;
////        long inchesDenominator = 5;
////        
////        String millimeterToString = "20.32";
////        measurement = new Measurement(inchesNumerator, inchesDenominator);
////        assertThat(millimeterToString).isEqualTo(measurement.toString());
////        
////        String inchesToString = "4/5";
////        measurement = new Measurement(inchesNumerator, inchesDenominator);
////        assertThat(inchesToString).isEqualTo(measurement.toString());
////    }
//    
//    @After
//    public void tearDown() {
//        firstMeasurement = null;
//        secondMeasurement = null;
//    }
//}
