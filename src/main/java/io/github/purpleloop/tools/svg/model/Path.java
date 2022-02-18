package io.github.purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Optional;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.tools.svg.tools.StyleUtils;

/**
 * Represents an SVG path.
 */
public class Path extends SvgObject {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(Path.class);

    private GeneralPath path;

    // Specfic Inkscape label ...
    private String inkscapeLabel;

    private double usualDistance = 0.0;

    public Path(String id, String pathDescription) {
        super(id);

        path = new GeneralPath();

        parsePath(pathDescription);
    }

    private Path(String id, GeneralPath other) {
        super(id);

        path = other;
    }

    private void parsePath(String pathDescription) {

        PathParser pathParser = new PathParser(pathDescription);

        String pathCommandString;

        PathCommand command = null;

        boolean relativeMove = false;
        try {

            Point referencePoint = new Point(0.0, 0.0);

            while (pathParser.hasNext()) {

                // See ahead for the command (may be omitted)
                pathCommandString = pathParser.peekNextPart();

                Optional<PathCommand> commandOptional = PathCommand.getCommand(pathCommandString);

                if (commandOptional.isPresent()) {
                    command = commandOptional.get();
                    relativeMove = command.isRelative(pathCommandString);

                    // Consume the command
                    pathParser.nextPart();

                } else if (command == null) {
                    LOG.error("Omitted command witout previous one");
                    break;
                }

                switch (command) {
                case MOVE:
                    referencePoint = parseMoveTo(pathParser, relativeMove, referencePoint);
                    break;

                case LINETO:
                    referencePoint = parseLineTo(pathParser, relativeMove, referencePoint);
                    break;

                case HORIZONTAL_LINETO:
                    referencePoint = parseHorizontalLine(pathParser, relativeMove, referencePoint);
                    break;

                case VERTICAL_LINETO:
                    referencePoint = parseVerticalLine(pathParser, relativeMove, referencePoint);
                    break;

                case CURVE:
                    referencePoint = parseCurve(pathParser, relativeMove, referencePoint);
                    break;

                case ARCH:
                    referencePoint = parseArch(pathParser, relativeMove, referencePoint);
                    break;

                case CLOSE:
                    LOG.debug("Closing path");
                    path.closePath();
                    break;

                default:

                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.error("Path analysis failed for id '" + getId());
            LOG.info("PathDescription = '" + pathDescription + "'");
            LOG.debug("Path processed : " + toString());
        }

    }

    private Point parseMoveTo(PathParser pathParser, boolean relativeMove, Point referencePoint) {

        Point targetPoint = Point.parseCoords(pathParser);

        // The first move 'm' should be treated as absolute
        boolean first = (pathParser.getIndex() == 1);
        if (first || relativeMove) {
            targetPoint.translate(referencePoint.getX(), referencePoint.getY());
        }

        float x = (float) targetPoint.getX();
        float y = (float) targetPoint.getY();

        path.moveTo(x, y);
        referencePoint = targetPoint;

        referencePoint = parseSuiteLineto(pathParser, relativeMove, referencePoint);
        return referencePoint;
    }

    /** Line */
    private Point parseLineTo(PathParser pathParser, boolean relativeMove, Point referencePoint) {
        Point targetPoint = Point.parseCoords(pathParser);

        if (relativeMove) {
            targetPoint.translate(referencePoint.getX(), referencePoint.getY());
        }

        path.lineTo((float) targetPoint.getX(), (float) targetPoint.getY());
        referencePoint = targetPoint;

        referencePoint = parseSuiteLineto(pathParser, relativeMove, referencePoint);
        return referencePoint;
    }

    /** The next part : a suite of implicit lineTos. */
    private Point parseSuiteLineto(PathParser pathParser, boolean relativeMove,
            Point referencePoint) {

        Point targetPoint;

        while (pathParser.nextIsNumber()) {
            targetPoint = Point.parseCoords(pathParser);

            if (relativeMove) {
                targetPoint.translate(referencePoint.getX(), referencePoint.getY());
            }

            path.lineTo((float) targetPoint.getX(), (float) targetPoint.getY());
            referencePoint = targetPoint;

        }
        return referencePoint;
    }

    /** Horizontal Line */
    private Point parseHorizontalLine(PathParser pathParser, boolean relativeMove,
            Point referencePoint) {
        double value = Double.parseDouble(pathParser.nextPart());

        Point p1 = new Point(value, 0);

        if (relativeMove) {
            p1.translate(referencePoint.getX(), referencePoint.getY());
        }
        referencePoint = new Point(p1);

        path.lineTo((float) p1.getX(), (float) p1.getY());
        return referencePoint;
    }

    /** Vertical Line */
    private Point parseVerticalLine(PathParser pathParser, boolean relativeMove,
            Point referencePoint) {
        double value = Double.parseDouble(pathParser.nextPart());

        Point p1 = new Point(0, value);

        if (relativeMove) {
            p1.translate(referencePoint.getX(), referencePoint.getY());
        }
        referencePoint = new Point(p1);

        path.lineTo((float) p1.getX(), (float) p1.getY());
        return referencePoint;
    }

    private Point parseCurve(PathParser pathParser, boolean relativeMove, Point referencePoint) {
        // Curve

        /* Le W3C spec 1.1 :
         * 
         * C (absolute) c (relative) curveto (x1 y1 x2 y2 x y)+ Draws a cubic
         * Bézier curve from the current point to (x,y) using (x1,y1) as the
         * control point at the beginning of the curve and (x2,y2) as the
         * control point at the end of the curve.
         * 
         * C (uppercase) indicates that absolute coordinates will follow; c
         * (lowercase) indicates that relative coordinates will follow. Multiple
         * sets of coordinates may be specified to draw a polybézier. At the end
         * of the command, the new current point becomes the final (x,y)
         * coordinate pair used in the polyb�zier. */

        Point p1, p2, p3;

        while (pathParser.nextIsNumber()) {

            p1 = Point.parseCoords(pathParser);

            if (relativeMove) {
                p1.translate(referencePoint.getX(), referencePoint.getY());
            }
            // reference = new Point(p1);

            p2 = Point.parseCoords(pathParser);

            if (relativeMove) {
                p2.translate(referencePoint.getX(), referencePoint.getY());
            }
            // reference = new Point(p2);

            p3 = Point.parseCoords(pathParser);

            if (relativeMove) {
                p3.translate(referencePoint.getX(), referencePoint.getY());
            }
            referencePoint = new Point(p3);

            LOG.debug("Bézier cubique (" + p1 + ") (" + p2 + "), (" + p3 + ")");

            path.curveTo((float) p1.getX(), (float) p1.getY(), (float) p2.getX(), (float) p2.getY(),
                    (float) p3.getX(), (float) p3.getY());

        }
        return referencePoint;
    }

    private Point parseArch(PathParser pathParser, boolean relativeMove, Point referencePoint) {

        // TODO arc support

        // rx ry x-axis-rotation large-arc-flag sweep-flag dx dy

        // Arc
        Point radius = Point.parseCoords(pathParser);
        double xAxisRotation = Double.parseDouble(pathParser.nextPart());
        int largeArcFlag = Integer.parseInt(pathParser.nextPart());
        int sweepFlag = Integer.parseInt(pathParser.nextPart());
        Point targetPoint = Point.parseCoords(pathParser);

        if (relativeMove) {
            targetPoint.translate(referencePoint.getX(), referencePoint.getY());
        }

        if (LOG.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Radius :");
            sb.append(radius);
            sb.append("X-axis-rotation :");
            sb.append(xAxisRotation);
            sb.append("Is large arc ?");
            sb.append(largeArcFlag);
            sb.append("Sweep arc ?");
            sb.append(sweepFlag);
            sb.append("dx,dy : ");
            sb.append(targetPoint);
            LOG.debug(sb.toString());
        }

        LOG.warn("Arcs are not supported / replaced by line");        
        
        /* parts.add(new EllipticArc(location, radius, xAxisRotation,
         * largeArcFlag == 0, sweepFlag == 0, pathCommand.equals("a"))); */

        path.lineTo((float) targetPoint.getX(), (float) targetPoint.getY());

        return targetPoint;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        /* for (PathPart part : parts) { sb.append(" "+part.toString());
         * sb.append("\n"); } */

        return "Path \n" + sb.toString();
    }

    @Override
    public void render(Graphics2D g, Stack<Transformation> transformationStack) {

        preTransform(transformationStack, g);

        StyleUtils.applyStyleFill(this, g);
        g.fill(path);
        StyleUtils.applyStyleDraw(this, g);
        g.draw(path);
        postTransform(g);

    }

    @Override
    public SvgObject selectIn(int x, int y) {

        boolean in = path.contains(x, y);

        return (in) ? this : null;
    }

    @Override
    public void move(int dx, int dy) {
        AffineTransform af = AffineTransform.getTranslateInstance(dx, dy);
        path.transform(af);
    }

    public Rectangle getBounds() {
        return path.getBounds();

    }

    public void setInkscapeLabel(String val) {
        this.inkscapeLabel = val;
    }

    public String getInkscapeLabel() {
        return inkscapeLabel;
    }

    public GeneralPath getPath() {
        return path;
    }

    public static Path fromPolygon(String id, String pointList) {

        GeneralPath path = new GeneralPath();
        String[] partsArray = pointList.split(" ");

        String abs, ord;
        double x, y;

        boolean already = false;

        for (String pointDesc : partsArray) {

            int pos = pointDesc.indexOf(',');
            abs = pointDesc.substring(0, pos);
            ord = pointDesc.substring(pos + 1, pointDesc.length());

            x = Double.parseDouble(abs);
            y = Double.parseDouble(ord);

            if (already) {

                path.lineTo(x, y);
            } else {

                already = true;
                path.moveTo(x, y);
            }

        }
        path.closePath();

        Path built = new Path(id, path);

        return built;

    }

}
