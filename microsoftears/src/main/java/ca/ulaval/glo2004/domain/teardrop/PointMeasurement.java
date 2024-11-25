package ca.ulaval.glo2004.domain.teardrop;

import java.io.Serializable;

/**
 * Class used to represent a 2d point or a 2d vector
 * using Measurements.
 */
public class PointMeasurement implements Cloneable, Serializable {
    public Measurement x;
    public Measurement y;

    public PointMeasurement(Measurement x, Measurement y) {
        this.x = x;
        this.y = y;
    }
    
    public PointMeasurement(double x, double y) {
        this.x = new Measurement(x);
        this.y = new Measurement(y);
    }

    public Measurement getX() {
        return x;
    }

    public Measurement getY() {
        return y;
    }
    
    public PointMeasurement sub(PointMeasurement other) {
        return new PointMeasurement(
                x.sub(other.getX()),
                y.sub(other.getY())
        );
    }
    
    public PointMeasurement add(PointMeasurement other) {
        return new PointMeasurement(
                x.add(other.getX()),
                y.add(other.getY())
        );
    }

    public PointMeasurement mult(Measurement other) {
        return new PointMeasurement(
                x.mult(other),
                y.mult(other)
        );
    }

    public PointMeasurement mult(int other) {
        return new PointMeasurement(
                x.mult(other),
                y.mult(other)
        );
    }

    public PointMeasurement div(Measurement other) {
        return new PointMeasurement(
                x.div(other),
                y.div(other)
        );
    }

    public PointMeasurement div (int other) {
        return new PointMeasurement(
                x.div(2),
                y.div(2)
        );
    }

    public PointMeasurement normalize() {
        return this.div(this.norm());
    }

    public Measurement norm() {
        return this.x.power2().add(this.y.power2()).sqrt();
    }
    
    public Measurement dot(PointMeasurement other) {
        return this.x.mult(other.getX()).add(this.y.mult(other.getY()));
    }
    
    @Override
    public String toString() {
        return x.toString() + ", " + y.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PointMeasurement)) {
            return false;
        }
        PointMeasurement otherPoint = (PointMeasurement) o;
        return x.equals(otherPoint.getX()) && y.equals(otherPoint.getY());
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
