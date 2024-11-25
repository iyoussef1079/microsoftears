package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import ca.ulaval.glo2004.domain.controller.UserException;
import ca.ulaval.glo2004.domain.teardrop.mcmastercarr.StrutCatalog;
import ca.ulaval.glo2004.domain.teardrop.mcmastercarr.StrutEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Strut implements IDrawable, Serializable, Cloneable {
    public static final int SPRINGS_USED = 2; // 2 springs for 1 hatch

    private static final Measurement DISTANCE_STRUT_HATCH = new Measurement(1, Unit.INCHES);
    private static final double NEWTONS_TO_POUNDS = 0.224809;

    // Warning: these properties should not be modified externally!
    private StrutEntry strutEntry = null;
    private double extensionForcePounds = -1;
    private PointMeasurement hatchAttachmentPosition;
    private PointMeasurement sideWallAttachmentPosition;
    private SegmentMeasurement segment = new SegmentMeasurement(
        hatchAttachmentPosition,
        sideWallAttachmentPosition
    );

    public SegmentMeasurement getSegment() {
        return segment;
    }

    public void setSegment(SegmentMeasurement segment) {
        this.segment = segment;
    }

    private CustomPolygon polygon;
    private DisplayMode displayMode;
    private final TearDropTrailer tearDropTrailer;

    public Strut(TearDropTrailer tearDropTrailer) {
        this.tearDropTrailer = tearDropTrailer;
        this.displayMode = DisplayMode.REGULAR;
    }

    private StrutEntry getStrutEntry() {
        if (this.strutEntry == null) {
            this.strutEntry = StrutCatalog.getStrutEntryByExtendedLength(this.tearDropTrailer.getHatch().getOverallLength().mult(0.6));
        }
        return this.strutEntry;
    }

    public Measurement getExtendedLength() {
        return this.getStrutEntry().getExtendedLength();
    }

    public Measurement getStrokeLength() {
        return this.getStrutEntry().getStrokeLength();
    }

    public String getId() {
        return this.getStrutEntry().getId();
    }

    public double getRequiredExtensionForcePounds() {
        if (this.extensionForcePounds == -1) {
            // calculations made by Dan Lott, extracted from his Excel spreadsheet
            // sorry for the unnamed constants, that's how life goes!
            double deadWeightNewtons = this.tearDropTrailer.getHatch().getDeadWeightPounds() * 4.4482216;
            double centerOfGravityLengthMM = this.tearDropTrailer.getHatch().getOverallLength().millimeter / 2;
            double powerArmLengthMM = this.getStrokeLength().mult(0.85).millimeter;
            double forceRequiredNewtons = (deadWeightNewtons * centerOfGravityLengthMM) / (powerArmLengthMM * SPRINGS_USED);
            double safetyFactorNewtons = -1;
            if (deadWeightNewtons < 300) {
                safetyFactorNewtons = 0.1 * forceRequiredNewtons;
            } else {
                safetyFactorNewtons = 50;
            }
            this.extensionForcePounds = (forceRequiredNewtons + safetyFactorNewtons) * NEWTONS_TO_POUNDS;
        }
        return this.extensionForcePounds;
    }

    public double getActualExtensionForcePounds() {
        double requiredExtensionForcePounds = getRequiredExtensionForcePounds();
        double actualExtensionForce = -1;
        List<Integer> availableExtensionForces = strutEntry.getExtensionForcePounds();

        // find first available strut extension force that has at least the required strut force
        for (Integer availableExtensionForce : availableExtensionForces) {
            if (availableExtensionForce >= requiredExtensionForcePounds) {
                actualExtensionForce = availableExtensionForce;
                break;
            }
        }

        if (actualExtensionForce == -1) {
            actualExtensionForce = availableExtensionForces.get(availableExtensionForces.size()-1);
        }
        return actualExtensionForce;
    }

    public PointMeasurement getSideWallAttachmentPosition() {
        if (this.sideWallAttachmentPosition == null) {
            PointMeasurement hingeCenterPosition = this.tearDropTrailer.getHatch().getHingeCenterPosition();

            Measurement targetDistanceFromHinge = this.getExtendedLength();
            List<PointMeasurement> innerVertices = this.tearDropTrailer.getHatch().getInnerVertices();
            for (int i = 0; i < innerVertices.size() - 1; i++) {
                PointMeasurement parallelVector = innerVertices.get(i + 1).sub(innerVertices.get(i)).normalize();
                PointMeasurement perpendicularVector = new PointMeasurement(
                        parallelVector.getY().mult(-1),
                        parallelVector.getX()
                );
                PointMeasurement candidateAttachment = innerVertices.get(i).add(perpendicularVector.mult(DISTANCE_STRUT_HATCH));

                // 10mm seems fair
                if (GeometricUtils.getDistance(candidateAttachment, hingeCenterPosition).compareTo(targetDistanceFromHinge) <= 0) {
                    this.sideWallAttachmentPosition = candidateAttachment;
                    break;
                }
            }
            if (this.sideWallAttachmentPosition == null) {
                throw new UserException("Position d'attache du ressort au mur invalide");
            }
        }
        segment.setEndPoint(this.sideWallAttachmentPosition);
        return this.sideWallAttachmentPosition;
    }

    public PointMeasurement getHatchAttachmentPosition() {
        if (this.hatchAttachmentPosition == null) {
            PointMeasurement hingeCenterPosition = this.tearDropTrailer.getHatch().getHingeCenterPosition();
            Measurement targetDistanceFromHinge = this.getStrokeLength().mult(0.85);
            List<PointMeasurement> innerVertices = this.tearDropTrailer.getHatch().getInnerVertices();
            for (int i = 0; i < innerVertices.size() - 1; i++) {
                PointMeasurement parallelVector = innerVertices.get(i + 1).sub(innerVertices.get(i)).normalize();
                PointMeasurement perpendicularVector = new PointMeasurement(
                        parallelVector.getY().mult(-1),
                        parallelVector.getX()
                );
                PointMeasurement candidateAttachment = innerVertices.get(i).add(perpendicularVector.mult(DISTANCE_STRUT_HATCH));

                // 10mm seems fair
                if (GeometricUtils.getDistance(candidateAttachment, hingeCenterPosition).compareTo(targetDistanceFromHinge) <= 0) {
                    this.hatchAttachmentPosition = candidateAttachment;
                    break;
                }
                if (i == innerVertices.size() - 2) {
                    System.out.println("Position d'attache du ressort au hayon invalide?");
                    this.hatchAttachmentPosition = candidateAttachment;
                }
            }
        }
        segment.setStartPoint(hatchAttachmentPosition);
        return this.hatchAttachmentPosition;
    }

    @Override
    public CustomPolygon getPolygon() {
        if (this.polygon == null) {
            ArrayList<PointMeasurement> vertices = new ArrayList<>();
            vertices.add(this.getSideWallAttachmentPosition());
            vertices.add(this.getHatchAttachmentPosition());
            vertices.add(this.getSideWallAttachmentPosition());
            this.polygon = new CustomPolygon(vertices);
        }
        return this.polygon;
    }

    @Override
    public void invalidatePolygon() {
        this.polygon = null;
        this.strutEntry = null;
        this.extensionForcePounds = -1;
        this.sideWallAttachmentPosition = null;
        this.hatchAttachmentPosition = null;
    }

    @Override
    public void setVerticesPerInch(int verticesPerInch) {

    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getVertexCount() {
        return 0;
    }

    @Override
    public DisplayMode getDisplayMode() {
        return this.displayMode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void moove(PointMeasurement point) {

    }
}
