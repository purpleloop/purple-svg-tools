package purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** A SVG document. */
public class SvgDocument extends SvgContainer {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(SvgDocument.class);

    /** Document's width. */
    private double width;

    /** Document's height. */
    private double height;

    /** Map of all SVG element id's in the document for a direct access. */
    private Map<String, SvgObject> mapId;

    /** Viewbox. */
    private SvgViewBox viewBox;

    /**
     * Creates a SVG document with the given Id.
     * 
     * @param id document ID.
     */
    public SvgDocument(String id) {
        super(id);
    }

    /** Creates a SVG document. */
    public SvgDocument() {
        // TODO See if if it's a convention
        this("svg2");
    }

    /**
     * Sets the size of the document.
     * 
     * @param width width of the document
     * @param height height of the document
     */
    public void setSize(Double width, Double height) {
        this.width = width;
        this.height = height;
    }

    /** @return width of the document */
    public double getWidth() {
        return width;
    }

    /** @return height of the document */
    public double getHeight() {
        return height;
    }

    /** Registers all element ids. */
    public void registerIds() {

        mapId = new HashMap<String, SvgObject>();
        registerIds(mapId);

        for (String id : mapId.keySet()) {
            LOG.debug(id);
        }

    }

    /** @return all ids of the document */
    public Set<String> getIds() {
        return Collections.unmodifiableSet(mapId.keySet());
    }

    /**
     * Gives an SVG object by it's id
     * 
     * @param id requested id
     * @return SVG object
     */
    public SvgObject getObjectById(String id) {

        if (!mapId.containsKey(id)) {
            return null;
        }

        return mapId.get(id);
    }

    /**
     * Saves the current SVG document to a PNG file.
     * 
     * @throws IOException in case of IO error
     */
    public void savePNGToFile(File file) throws SvgException {

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(file);) {
            RenderedImage image = renderImage();
            boolean ok = ImageIO.write(image, "png", ios);

            if (!ok) {
                throw new SvgException(
                        "PNG export failed as no PNG image writer was found to export the document.");
            }

            ios.flush();
        } catch (IOException e) {
            throw new SvgException("an error occured while exporting the document to a PNG file.",
                    e);
        }

    }

    private RenderedImage renderImage() {

        BufferedImage bim = new BufferedImage(500/* (int) width.doubleValue() */,
                500/* ( int ) height . doubleValue ( ) */, BufferedImage.TYPE_INT_RGB);

        Graphics2D gra = bim.createGraphics();

        Stack<Transformation> trans = new Stack<Transformation>();

        render(gra, trans);
        return bim;
    }

    public void render(Graphics2D g2) {
        Stack<Transformation> transformations = new Stack<Transformation>();

        AffineTransform affineTransform = new AffineTransform();

        if (viewBox != null) {

            affineTransform.scale(width / viewBox.getWidth(), height / viewBox.getHeight());
            Matrix matrix = new Matrix(affineTransform);
            transformations.add(matrix);
        }

        render(g2, transformations);
    }

    public void setViewBox(SvgViewBox viewBox) {
        this.viewBox = viewBox;
    }

}
