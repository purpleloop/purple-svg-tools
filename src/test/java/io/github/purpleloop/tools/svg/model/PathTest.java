package io.github.purpleloop.tools.svg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.swing.image.ImageUtils;
import io.github.purpleloop.commons.swing.image.ImageUtils.FileFormat;

/** Tests on paths. */
class PathTest {

    /** Logger of the class. */
    private static final Log LOG = LogFactory.getLog(PathTest.class);

    @Test
    void testSquarePathAbsolute() {

        Path svgPath = new Path("dummy", "M 10,10 L 10,20 L 20,20 L 20,10 Z");

        assertPathCoordinates(svgPath.getPath(), 10.0, 10.0, 10.0, 20.0, 20.0, 20.0, 20.0, 10.0);
        exportResultingImage(svgPath, "target/testSquarePathAbsolute.jpg");
    }

    @Test
    void testSquarePathRelative() {

        Path svgPath = new Path("dummy", "m 10,10 l 0,20 l 20,0 l 0,-20 z");

        assertPathCoordinates(svgPath.getPath(), 10.0, 10.0, 10.0, 30.0, 30.0, 30.0, 30.0, 10.0);
        exportResultingImage(svgPath, "target/testSquarePathRelative.jpg");
    }

    @Test
    void testSquarePathHV() {

        Path svgPath = new Path("dummy", "m 10,10 v 50 h 50 v -50 z");

        assertPathCoordinates(svgPath.getPath(), 10.0, 10.0, 10.0, 60.0, 60.0, 60.0, 60.0, 10.0);
        exportResultingImage(svgPath, "target/testSquarePathHV.jpg");
    }

    /**
     * Assert that the requested path parts are reached. Remark : closing does
     * not return any point so no match is expected on it)
     * 
     * @param path the path to check
     * @param expectedCoord expected coordinates
     */
    private void assertPathCoordinates(GeneralPath path, double... expectedCoord) {
        double[] coords = new double[2];
        PathIterator pi = path.getPathIterator(new AffineTransform());

        int index = 0;
        while (index < expectedCoord.length) {

            assert (!pi.isDone());

            pi.currentSegment(coords);
            LOG.info("Check " + expectedCoord[index] + " =? " + coords[0]);
            assertEquals(expectedCoord[index], coords[0]);

            LOG.info("Check " + expectedCoord[index + 1] + " =? " + coords[1]);
            assertEquals(expectedCoord[index + 1], coords[1]);

            pi.next();
            index += 2;
        }

    }

    /**
     * Exports the given path to an image file
     * 
     * @param fileName the name of the file to create
     */
    private void exportResultingImage(Path svgPath, String fileName) {
        BufferedImage bim = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = ((Graphics2D) (bim.getGraphics()));
        g2.draw(svgPath.getPath());

        try {
            ImageUtils.saveImageToFile(bim, new File(fileName), FileFormat.JPG);
        } catch (PurpleException e) {
            LOG.error("Error while exporting SVG path", e);
        }
    }

}
