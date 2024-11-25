package ca.ulaval.glo2004.domain.drawing;

import ca.ulaval.glo2004.domain.controller.DisplayMode;
import ca.ulaval.glo2004.domain.teardrop.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SvgDrawer {
    private static final Measurement PADDING = new Measurement(2, Unit.INCHES);

    public static void saveSVG(TearDropTrailer tearDrop,  File file) throws IOException {
        RawProfile rawProfile = tearDrop.getRawProfile();
        double viewBoxWidth = rawProfile.getWidth().add(PADDING.mult(2)).getRoundedInches();
        double viewBoxHeight = rawProfile.getHeight().add(PADDING.mult(2)).getRoundedInches();
        double viewBoxMinX = rawProfile.getCornerPosition(Quadrant.TOP_LEFT).getX().sub(PADDING).getRoundedInches();
        double viewBoxMinY = rawProfile.getCornerPosition(Quadrant.TOP_LEFT).getY().sub(PADDING).getRoundedInches();

        FileWriter fileWriter = new FileWriter(file);
        // https://www.w3.org/TR/SVG2/coords.html#ViewBoxAttribute
        fileWriter.write(String.format("<svg version=\"1.1\"\n" +
                "     viewBox=\"%f %f %f %f\"\n" +
                "     height=\"500\"\n" +
                "     width=\"500\"\n" +
                "     xmlns=\"http://www.w3.org/2000/svg\">", viewBoxMinX, viewBoxMinY, viewBoxWidth, viewBoxHeight));

        for (IDrawable drawable : tearDrop.getDrawables()) {
            if (!drawable.getDisplayMode().equals(DisplayMode.HIDDEN)) {
                StringBuilder polylineElement = new StringBuilder("<polyline points=\"");
                for (PointMeasurement vertex : drawable.getPolygon().getVertices()) {
                    polylineElement.append(String.format("%f %f ", vertex.getX().getRoundedInches(), vertex.getY().getRoundedInches()));
                }
                polylineElement.append("\" style=\"fill:none;stroke:black;stroke-width:0.2\" />");
                fileWriter.write(polylineElement.toString());
            }
        }

        fileWriter.write("</svg>");
        fileWriter.close();
    }
}
