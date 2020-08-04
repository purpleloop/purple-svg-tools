package io.github.purpleloop.tools.svg.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

import io.github.purpleloop.tools.svg.model.SvgDocument;

/** A panel used to render SVG documents. */
public class SvgRenderPanel extends JPanel {

    /** Serial tag. */
    private static final long serialVersionUID = -2212959073155102444L;

    /** The document to render. */
    private SvgDocument svgDoc;

    /** Path to display when there is no document to render. */
    private GeneralPath brokenDocumentPath;

    /** Creates the render panel. */
    public SvgRenderPanel() {
        super();
        setPreferredSize(new Dimension(500, 500));
        
        createBrokenDocumentPath();
    }

    /** @param svgDoc set the document to render */
    public void setDocument(SvgDocument svgDoc) {
        this.svgDoc = svgDoc;
    }

    @Override
    public void paint(Graphics graphics) {

        super.paint(graphics);

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        if (svgDoc != null) {
            svgDoc.render(g2);
        } else {                        
            g2.drawString("There is no document bound to this area.", 20, 20);
            g2.draw(brokenDocumentPath);
        }
    }

    /** Prepares a path to display when there is no document to render. */
    private void createBrokenDocumentPath() {
        brokenDocumentPath = new GeneralPath();
        brokenDocumentPath.moveTo(50.0, 50.0);
        brokenDocumentPath.lineTo(145.0, 50.0);
        brokenDocumentPath.lineTo(50.0, 200.0);
        brokenDocumentPath.closePath();
        
        brokenDocumentPath.moveTo(150.0, 55.0);
        brokenDocumentPath.lineTo(150.0, 200.0);
        brokenDocumentPath.lineTo(55.0, 200.0);
        brokenDocumentPath.closePath();
    }

}
