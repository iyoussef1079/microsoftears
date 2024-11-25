package ca.ulaval.glo2004.domain.teardrop.mcmastercarr;

import ca.ulaval.glo2004.domain.teardrop.Measurement;
import ca.ulaval.glo2004.domain.teardrop.Unit;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;

public class StrutCatalog implements Serializable {
    private static final ArrayList<StrutEntry> availableStrutEntries = new ArrayList<>(
            // https://www.mcmaster.com/gas-struts/gas-springs-7/
            Arrays.asList(
                    new StrutEntry(7.01, 1.97, Arrays.asList(15, 20, 40, 60, 90), "4138T51"),
                    new StrutEntry(7.4, 2.36, Arrays.asList(15, 20, 30, 40, 50, 60, 80), "4138T52"),
                    new StrutEntry(9.65, 3.54, Arrays.asList(15, 20, 30, 40, 50, 60, 80), "4138T53"),
                    new StrutEntry(12.2, 3.94, Arrays.asList(15, 20, 30, 40, 50, 60, 90, 120), "4138T54"),
                    new StrutEntry(13.19, 4.92, Arrays.asList(15, 20, 30, 40, 50, 60, 90, 120), "4138T71"),
                    new StrutEntry(15.24, 5.47, Arrays.asList(15, 20, 30, 40, 50, 60, 90, 120), "4138T55"),
                    new StrutEntry(17.13, 6.3, Arrays.asList(15, 20, 30, 40, 50, 60, 90, 120), "4138T56"),
                    new StrutEntry(19.72, 7.87, Arrays.asList(15, 20, 30, 40, 50, 60, 90, 120), "4138T57"),
                    new StrutEntry(20.12, 8.27, Arrays.asList(15, 20, 30, 40, 50, 60, 90, 120), "4138T58"),
                    new StrutEntry(27.87, 10.24, Arrays.asList(50, 100, 150, 200, 250), "4138T61"),
                    new StrutEntry(29.49, 12.8, Arrays.asList(50, 100, 150, 200, 250), "4138T62"),
                    new StrutEntry(35.43, 16.14, Arrays.asList(50, 100, 150, 200, 250), "4138T63")

            )
    );

    public static StrutEntry getStrutEntryByExtendedLength(Measurement desiredExtendedLength) {
        Measurement closestDistance = new Measurement(99999, Unit.INCHES);
        StrutEntry bestStrutEntry = null;
        for (StrutEntry strutEntry : availableStrutEntries) {
            Measurement distance = strutEntry.getExtendedLength().sub(desiredExtendedLength).abs();
            if (closestDistance.compareTo(distance) > 0) {
                closestDistance = distance;
                bestStrutEntry = strutEntry;
            }
        }
        return bestStrutEntry;
    }
}
