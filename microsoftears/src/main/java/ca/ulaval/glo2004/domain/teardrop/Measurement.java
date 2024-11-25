package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.UserException;
import java.io.Serializable;

public class Measurement implements Comparable<Measurement>, Cloneable, Serializable {
    private static final double MAX_RELATIVE_ERROR = 1e-5;

    protected double inches;
    protected double millimeter;
    protected Unit unit;

    public Measurement(long inchesNumerator, long inchesDenominator) {
        if (inchesDenominator == 0) {
            throw new UserException("Problem with the measurement: Divider must not be 0!");
        }
        this.inches = (double) inchesNumerator / inchesDenominator;
        this.millimeter = 25.4 * inchesNumerator / inchesDenominator;
        this.unit = Unit.INCHES;
    }

    public Measurement(double millimeter) {
        this.inches = millimeter / 25.4;
        this.millimeter = millimeter;
        this.unit = Unit.MILLIMETERS;
    }


    public Measurement(double value, Unit unit) {
        if (unit.equals(Unit.MILLIMETERS)) {
            this.millimeter = value;
            this.inches = this.millimeter / 25.4;
        } else {
            this.millimeter = value * 25.4;
            this.inches = value;
        }
    }

    public static Measurement oneInch() {
        return new Measurement(1, Unit.INCHES);
    }

    public static Measurement zero() {
        return new Measurement(0);
    }
    
    public double getRoundedInches() {
        return this.inches;
    }

    public Measurement add(Measurement other) {
        return new Measurement(this.millimeter + other.millimeter);
    }
    
    public Measurement sub(Measurement other) {
        return new Measurement(this.millimeter - other.millimeter);
    }

    public Measurement abs() {
        if (this.compareTo(Measurement.zero()) < 0) {
            return this.mult(-1);
        }
        return this;
    }
    
    public Measurement mult(Measurement other) {
        return new Measurement(this.millimeter * other.millimeter);
    }
    
    public Measurement mult(double d) {
        return new Measurement(this.millimeter * d);
    }
    
    public Measurement mult(int other) {
        return new Measurement(this.millimeter * other);
    }    
    
    public Measurement div(Measurement other) {
        return new Measurement(this.millimeter / other.millimeter);
    }
    
    public Measurement div(double other) {
        return new Measurement(this.millimeter / other);
    }
    
    public Measurement div(int other) {
        return new Measurement(this.millimeter / other);
    }
    
    public Measurement power2() {
        return this.mult(this);
    }
    
    public Measurement power(int exp)
    {
        if(exp<0) throw new UserException("Ne fait pas ça s'il te plait"); //Measurement n'a pas acces au userException...// bon finalement laisse tomber
        Measurement copy = new Measurement(this.inches, unit.INCHES);
        for(int i=0; i<exp; i++)
        {
            this.mult(copy);
        }
        return this;
    }
    public Measurement sqrt() {
        double sqrtRootMillimiter = Math.sqrt(this.millimeter);
        return new Measurement(sqrtRootMillimiter);
    }
    
    @Override
    public int compareTo(Measurement other) {
        double relativeDifference = (this.millimeter - other.millimeter) / Math.abs(this.millimeter);
        if (Math.abs(relativeDifference) <= MAX_RELATIVE_ERROR) {
            return 0;
        } else {
            if (relativeDifference > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Measurement)) {
            return false;
        }
        Measurement measurement = (Measurement) other;
        return this.compareTo(measurement) == 0;
    }
    
    public String toString(Unit wanted_unit) {
        if(wanted_unit == Unit.MILLIMETERS)
            return String.format(java.util.Locale.US, "%.3f", this.millimeter);
        else
            return String.format(java.util.Locale.US, "%.3f", this.inches);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
