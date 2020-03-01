package purpleloop.tools.svg.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import purpleloop.tools.svg.tools.XmlTools;

public class SvgBuilder {

    private static final Log LOG = LogFactory.getLog(SvgBuilder.class);

    public static SvgDocument analyseSvgDocument(Element element) {

        if (!element.getNodeName().equalsIgnoreCase(SvgTags.ELT_SVG)) {
            LOG.error("Unexpected element " + element.getNodeName());
            return null;
        }

        String id = element.getAttribute(SvgTags.ID);
        SvgDocument svg = new SvgDocument(id);

        String widthStr = element.getAttribute(SvgTags.WIDTH);
        String heightStr = element.getAttribute(SvgTags.HEIGHT);
        Double width = Length.parse(widthStr);
        Double height = Length.parse(heightStr);
        svg.setSize(width, height);

        LOG.info("SVG document size : " + width + " x " + height);

        SvgViewBox viewBox = analyseViewBox(element.getAttribute(SvgTags.VIEW_BOX));
        if (viewBox != null) {

            svg.setViewBox(viewBox);
        }

        Style baseStyle = new Style();

        svg.setStyle(baseStyle);

        addSvgChildrenElements(svg, element, baseStyle);
        return svg;
    }

    private static SvgViewBox analyseViewBox(String viewBoxStr) {
        // Viewbox specifies how to scale
        if (viewBoxStr.isBlank()) {

            return null;
        }

        SvgViewBox box = new SvgViewBox();

        String[] viewBoxCoords = viewBoxStr.split(" ");
        box.setMinX(Double.parseDouble(viewBoxCoords[0]));
        box.setMinY(Double.parseDouble(viewBoxCoords[1]));
        box.setWidth(Double.parseDouble(viewBoxCoords[2]));
        box.setHeight(Double.parseDouble(viewBoxCoords[3]));

        return box;

    }

    private static void addSvgChildrenElements(SvgContainer container, Element elt,
            Style parentStyle) {
        SvgObject svgObject;
        List<Element> childrenElements = XmlTools.getChildrenElements(elt);
        for (Element childElement : childrenElements) {

            try {

                svgObject = analyseSvgElement(childElement, parentStyle);
                if (svgObject != null) {
                    container.addObject(svgObject);
                    svgObject.setParent(container);
                }

            } catch (Exception e) {
                LOG.error("Error during SVG element analysis " + childElement.getNodeName(), e);
            }
        }

    }

    private static SvgObject analyseSvgElement(Element elt, Style parentStyle) {

        String id = elt.getAttribute(SvgTags.ID);
        String styleStr = elt.getAttribute(SvgTags.ATT_STYLE);

        Style currentStyle = parentStyle;
        if (styleStr != null) {
            LOG.debug("Style :" + styleStr);
            currentStyle = Style.parseCss(parentStyle, styleStr);
        }

        SvgObject result = null;

        String name = elt.getNodeName();
        if (name.equals(SvgTags.FLOW_ROOT)) {

            result = analyseSvgFlowRootElement(id, elt, currentStyle);

        } else if (name.equals(SvgTags.PATH)) {
            result = analyseSvgPathElement(elt, id);

        } else if (name.equals(SvgTags.FLOW_PARA)) {
            result = new Paragraph(id, elt.getTextContent());

        } else if (name.equals(SvgTags.GROUP)) {

            Group grp = new Group(id);
            addSvgChildrenElements(grp, elt, currentStyle);
            result = grp;

        } else if (name.equals(SvgTags.FLOW_REGION)) {

            LOG.debug("Ignore flow region");
            result = null;

        } else if (name.equals(SvgTags.RECTANGLE)) {
            result = analyseSvgRectangleElement(elt, id);

        } else if (name.equals(SvgTags.CIRCLE)) {
            result = analyseSvgCircleElement(elt, id);

        } else if (name.equals(SvgTags.ELLIPSE)) {
            result = analyseSvgEllipseElement(elt, id);

        } else if (name.equals(SvgTags.ELT_DEFS)) {
            result = analyseSvgDefElement(elt, id);

        } else if (name.equals(SvgTags.ELT_POLYGON)) {
            result = analyseSvgPolygonElement(elt, id);

        } else if (name.equals(SvgTags.ELT_IMAGE)) {
            result = analyseSvgImageElement(elt, id);

        } else if (SvgTags.getIgnoreList().contains(name)) {

            // Ignore element
            LOG.info("XML-DOM element tagName ='" + name
                    + "' is not supported and has been ignored.");
            result = null;

        } else {
            // Unknown element
            LOG.error("XML-DOM element tagName ='" + name
                    + "' is not known by the current building proces "
                    + " All occurences of the tag will be ignorded.");
            SvgTags.updateIgnoreList(name);
            result = null;
        }

        if (result != null) {

            if (currentStyle != null) {
                result.setStyle(currentStyle);
            }
        }

        return result;

    }

    private static Path analyseSvgPolygonElement(Element elt, String id) {

        String pointList = elt.getAttribute(SvgTags.POINTS);
        Path pathFromPolygon = Path.fromPolygon(id, pointList);
        pathFromPolygon.setInkscapeLabel(elt.getAttribute(SvgTags.INKSCAPE_LABEL));
        return pathFromPolygon;
    }

    private static SvgObject analyseSvgFlowRootElement(String id, Element elt, Style style) {
        FlowRoot flowRoot = new FlowRoot(id);

        String transformationStr = elt.getAttribute(SvgTags.ATT_TRANSFORMATION);

        if (StringUtils.isNotBlank(transformationStr)) {

            Transformation trans = TransformationUtils.parse(transformationStr);
            flowRoot.setTransformation(trans);
        }

        addSvgChildrenElements(flowRoot, elt, style);
        return flowRoot;
    }

    /**
     * Definitions.
     * 
     * @param id
     * @return
     */
    private static SvgDefContainer analyseSvgDefElement(Element elt, String id) {

        SvgDefContainer defContainer = new SvgDefContainer(id);
        LOG.debug("Definitions " + id);

        List<Element> childrenElements = XmlTools.getChildrenElements(elt);
        for (Element childElement : childrenElements) {

            SvgObject definition = analyseSvgContentsDefElement(childElement);

            if (definition != null && (definition instanceof SvgDefinition)) {

                defContainer.add((SvgDefinition) definition);
                definition.setParent(defContainer);
            }
        }

        return defContainer;

    }

    private static SvgObject analyseSvgContentsDefElement(Element elt) {

        String id = elt.getAttribute(SvgTags.ID);
        String name = elt.getNodeName();
        if (name.equals(SvgTags.ELT_MARKER)) {
            // TODO Manage markers
            LOG.warn("Markers are noy yet implmemented");
            return null;

        } else if (name.equals(SvgTags.ELT_LINEAR_GRADIENT)) {
            return analyseSvgLinearGradientElement(elt, id);

        } else if (name.equals(SvgTags.ELT_RADIAL_GRADIENT)) {
            return analyseSvgRadialGradientElement(elt, id);

        } else {
            LOG.warn("Element type unsupported " + name);
            return null;
        }
    }

    private static SvgObject analyseSvgRadialGradientElement(Element elt, String id) {

        SvgObject result = null;

        String referenceXLink = elt.getAttribute(SvgTags.ATT_XLINK_REFERENCE);

        if (StringUtils.isNotBlank(referenceXLink)) {
            LOG.debug("Reference : " + referenceXLink);

            String cx = elt.getAttribute(SvgTags.CX);
            String cy = elt.getAttribute(SvgTags.CY);
            Point centre = new Point(Double.parseDouble(cx), Double.parseDouble(cy));

            String fxStr = elt.getAttribute(SvgTags.FX);
            String fyStr = elt.getAttribute(SvgTags.FY);
            String rStr = elt.getAttribute(SvgTags.R);
            double fx = Double.parseDouble(fxStr);
            double fy = Double.parseDouble(fyStr);
            double r = Double.parseDouble(rStr);
            String transformationStr = elt.getAttribute(SvgTags.GRADIENT_TRANSFORMATION);
            Transformation transformation = TransformationUtils.parse(transformationStr);

            RadialGradientReference gradRef = new RadialGradientReference(id, referenceXLink,
                    centre, fx, fy, r);

            result = gradRef;

        }
        return result;
    }

    private static SvgObject analyseSvgLinearGradientElement(Element elt, String id) {

        SvgObject result = null;

        String referenceXLink = elt.getAttribute(SvgTags.ATT_XLINK_REFERENCE);

        if (StringUtils.isNotBlank(referenceXLink)) {
            LOG.debug("Reference : " + referenceXLink);

            String x1 = elt.getAttribute(SvgTags.X1);
            String y1 = elt.getAttribute(SvgTags.Y1);
            Point p1 = new Point(Double.parseDouble(x1), Double.parseDouble(y1));

            String x2 = elt.getAttribute(SvgTags.X2);
            String y2 = elt.getAttribute(SvgTags.Y2);
            Point p2 = new Point(Double.parseDouble(x2), Double.parseDouble(y2));

            LinearGradientReference gradRef = new LinearGradientReference(id, referenceXLink, p1,
                    p2);

            result = gradRef;

        } else {

            LOG.debug("Linear gradient");
            LinearGradientWithStops gradient = new LinearGradientWithStops(id);

            List<Element> childrenElements = XmlTools.getChildrenElements(elt);
            for (Element childElement : childrenElements) {
                gradient.addStop(analyseSvgGradientStopElement(childElement));
            }

            result = gradient;
        }

        return result;
    }

    private static GradientStop analyseSvgGradientStopElement(Element elt) {

        LOG.debug("Gradient Stop element");

        String id = elt.getAttribute(SvgTags.ID);

        String offsetStr = elt.getAttribute(SvgTags.ATT_OFFSET);
        double offset = Double.parseDouble(offsetStr);

        GradientStop stop = new GradientStop(id, offset);

        String style = elt.getAttribute(SvgTags.ATT_STYLE);

        LOG.debug("- Offset :" + offset);
        LOG.debug("- Style :" + style);

        Style stopStyle = Style.parseCss(new Style(), style);

        stop.setStyle(stopStyle);
        return stop;
    }

    /** Path. */
    private static Path analyseSvgPathElement(Element elt, String id) {

        String pathDescription = elt.getAttribute(SvgTags.PATH_STRING);

        Path p = new Path(id, pathDescription);

        p.setInkscapeLabel(elt.getAttribute(SvgTags.INKSCAPE_LABEL));

        return p;
    }

    private static Ellipse analyseSvgEllipseElement(Element elt, String id) {
        String cxStr = elt.getAttribute(SvgTags.CX);
        String cyStr = elt.getAttribute(SvgTags.CY);
        String rxStr = elt.getAttribute(SvgTags.RX);
        String ryStr = elt.getAttribute(SvgTags.RY);
        double cx = Double.parseDouble(cxStr);
        double cy = Double.parseDouble(cyStr);
        double rx = Double.parseDouble(rxStr);
        double ry = Double.parseDouble(ryStr);

        return new Ellipse(id, cx, cy, rx, ry);
    }

    private static Circle analyseSvgCircleElement(Element elt, String id) {
        String xStr = elt.getAttribute(SvgTags.CX);
        String yStr = elt.getAttribute(SvgTags.CY);
        String rStr = elt.getAttribute(SvgTags.RADIUS);
        double x = Double.parseDouble(xStr);
        double y = Double.parseDouble(yStr);
        double r = Double.parseDouble(rStr);

        return new Circle(id, x, y, r);
    }

    private static Rectangle analyseSvgRectangleElement(Element elt, String id) {

        String xStr = elt.getAttribute(SvgTags.X);
        String yStr = elt.getAttribute(SvgTags.Y);
        String widthStr = elt.getAttribute(SvgTags.WIDTH);
        String heightStr = elt.getAttribute(SvgTags.HEIGHT);
        double x = Double.parseDouble(xStr);
        double y = Double.parseDouble(yStr);
        double width = Double.parseDouble(widthStr);
        double height = Double.parseDouble(heightStr);

        return new Rectangle(id, x, y, width, height);
    }

    private static SvgObject analyseSvgImageElement(Element elt, String id) {
        LOG.info("Analysis of an image élémént " + id);

        String heightStr = elt.getAttribute("height");
        double height = readMeasure(heightStr);
        String widthStr = elt.getAttribute("width");
        double width = readMeasure(widthStr);
        String xlinkHRef = elt.getAttribute("xlink:href");

        // data:image/png;base64,iVBORw0...
        LOG.debug("Implemented : xlinkHRef = " + xlinkHRef);
        String[] xlinkHRefPart = xlinkHRef.split("[:,;]");

        String dataTypeStr = xlinkHRefPart[1];
        String type = dataTypeStr.substring(dataTypeStr.indexOf("/") + 1);
        LOG.info("dataTypeStr " + dataTypeStr + " = type " + type);

        String encoding = xlinkHRefPart[2];
        LOG.info("encoding " + encoding);// base64

        String base64DataString = xlinkHRefPart[3];
        LOG.debug("base64DataString " + base64DataString);

        Decoder decoder = java.util.Base64.getDecoder();

        base64DataString = base64DataString.replaceAll(" ", "");

        byte[] data = decoder.decode(base64DataString);

        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(bais);

            Iterator<ImageReader> readerIt = ImageIO.getImageReadersByFormatName(type);

            BufferedImage image = null;

            ImageReader reader;

            boolean found = false;
            while (!found && readerIt.hasNext()) {
                reader = readerIt.next();
                ImageReadParam param = reader.getDefaultReadParam();

                reader.setInput(iis, true, true);
                try {
                    image = reader.read(0, param);
                } finally {
                    reader.dispose();
                }
                if (image != null) {
                    return new SvgImage(id, image);
                }

                LOG.info("Unable to read image id=" + id + " with this reader " + reader);
            }

            throw new RuntimeException("Unable to read image id=" + id + " no mode readers.");

        } catch (IOException e) {
            throw new RuntimeException("Exception during image processing id=" + id, e);
        } finally {
            if (iis != null) {
                try {
                    iis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private static double readMeasure(String value) {

        double coef = 1.0;
        return coef * Double.parseDouble(value);
    }

    /**
     * Loads a SVG document from a file
     * 
     * @param f SVG file to load
     * @return SVG document
     */
    public static SvgDocument loadFromFile(File f) {

        try {
            Document doc = XmlTools.getDocument(f);

            Element racine = doc.getDocumentElement();
            SvgDocument svg = SvgBuilder.analyseSvgDocument(racine);

            LOG.debug("CSS properties found during analysis :");
            List<String> allp = Style.getAllProperties();
            Collections.sort(allp);
            for (String pk : allp) {
                LOG.debug(pk);
            }

            svg.registerIds();

            return svg;
        } catch (Exception e) {
            LOG.error("Error while reading XML file " + f, e);
            return null;
        }
    }

}
