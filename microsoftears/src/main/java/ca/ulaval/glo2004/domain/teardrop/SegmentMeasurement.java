package ca.ulaval.glo2004.domain.teardrop;

import java.io.Serializable;

public class SegmentMeasurement implements Serializable {
    protected PointMeasurement startPoint;
    protected PointMeasurement endPoint;
    
    public SegmentMeasurement (PointMeasurement startPoint, PointMeasurement endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public PointMeasurement getVector() {
        return endPoint.sub(startPoint);
    }

    public PointMeasurement getStartPoint(){
        return this.startPoint;
    }
    
    public PointMeasurement getEndPoint(){
        return this.endPoint;
    }

    public void setStartPoint(PointMeasurement startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(PointMeasurement endPoint) {
        this.endPoint = endPoint;
    }
    
    /**
     * Performs the dot product on two segments
     * @param other the other segment
     * @return The dot product
     */
    public Measurement dot(SegmentMeasurement other) {
        PointMeasurement thisVector = this.getEndPoint().sub(this.getStartPoint());
        PointMeasurement otherVector = other.getEndPoint().sub(other.getStartPoint());
        return thisVector.dot(otherVector);
    }
}
