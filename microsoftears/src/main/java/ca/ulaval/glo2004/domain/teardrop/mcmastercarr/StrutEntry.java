package ca.ulaval.glo2004.domain.teardrop.mcmastercarr;

import ca.ulaval.glo2004.domain.teardrop.Measurement;
import ca.ulaval.glo2004.domain.teardrop.Unit;
import java.io.Serializable;
import java.util.List;

public class StrutEntry implements Serializable {
    private final Measurement extendedLength;
    private final Measurement strokeLength;
    private final List<Integer> extensionForcePounds;
    private final String id;

    public StrutEntry(double extendedLengthInches, double strokeLengthInches, List<Integer> extensionForcePounds, String id) {
        this.extendedLength = new Measurement(extendedLengthInches, Unit.INCHES);
        this.strokeLength = new Measurement(strokeLengthInches, Unit.INCHES);
        this.extensionForcePounds = extensionForcePounds;
        this.id = id;
    }

    public Measurement getExtendedLength() {
        return extendedLength;
    }

    public Measurement getStrokeLength() {
        return strokeLength;
    }

    public List<Integer> getExtensionForcePounds() {
        return extensionForcePounds;
    }

    public String getId() {
        return id;
    }
}
