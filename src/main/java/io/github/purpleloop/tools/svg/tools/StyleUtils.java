package io.github.purpleloop.tools.svg.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.tools.svg.model.Style;
import io.github.purpleloop.tools.svg.model.SvgDefinition;
import io.github.purpleloop.tools.svg.model.SvgObject;

/** Style utilities. */
public class StyleUtils {

    /** Logger. */
    public static final Log LOG = LogFactory.getLog(StyleUtils.class);

    /** Color palette. */
    private static Map<String, Color> colorPalette;

    static {
        colorPalette = new HashMap<String, Color>();
        colorPalette.put("none", new Color(0, 0, 0, 0));
    }

    public static Color getColorFromHexString(String colStr, float transparency) {

        if (colStr.equals("none")) {
            return colorPalette.get("none");
        }

        if (colStr.equalsIgnoreCase("white")) {
            return Color.WHITE;
        }

        if (colStr.equalsIgnoreCase("black")) {
            return Color.BLACK;
        }

        String completeStr = colStr + "/alpha" + transparency;

        Color res = colorPalette.get(completeStr);

        if (res == null) {

            if (colStr.length() != 7) {
                throw new InvalidParameterException("Bad color format " + colStr);
            }

            String hexR = colStr.substring(1, 3);
            String hexG = colStr.substring(3, 5);
            String hexB = colStr.substring(5, 7);

            float r = (float) (Integer.parseInt(hexR, 16) / 256.0);
            float g = (float) (Integer.parseInt(hexG, 16) / 256.0);
            float b = (float) (Integer.parseInt(hexB, 16) / 256.0);

            res = new Color(r, g, b, transparency);

            colorPalette.put(completeStr, res);

        }

        return res;
    }

    public static Stroke getStrokeFormStyle(Style style) {

        String strokeWidStr = style.getProperty("stroke-width");

        // TODO Complete stroke support (line cap ant line join impl)
        //String strokeCap = style.getProperty("stroke-linecap");
        //String strokeLineJoin = style.getProperty("stroke-linejoin");

        // Stroke Width
        float strokWid = 1.0f;

        if (strokeWidStr != null) {
            if (strokeWidStr.endsWith("px")) {
                strokeWidStr = strokeWidStr.replaceFirst("px", "");
            }
            strokWid = Float.parseFloat(strokeWidStr);
        }

        // Cap butt
        // No decorations
        int cap = BasicStroke.CAP_BUTT; 

        // Join segments outside until meet
        int lineJoin = BasicStroke.JOIN_MITER;

        Stroke s = new BasicStroke(strokWid, cap, lineJoin);

        return s;
    }

    public static void applyStyleDraw(SvgObject target, Graphics2D g) {

        Style style = target.getStyle();

        String colStr = style.getProperty("stroke");
        String colOpacity = style.getProperty("stroke-opacity");

        float opacity = 1.0f;
        if (colOpacity != null) {
            opacity = Float.parseFloat(colOpacity);
        }

        if (colStr == null) {
            g.setColor(Color.BLACK);

        } else if (colStr.startsWith("url(")) {
            target.resolveUrl(colStr.substring(4, colStr.length() - 1));

        } else {
            g.setColor(StyleUtils.getColorFromHexString(colStr, opacity));
        }

        Stroke str = StyleUtils.getStrokeFormStyle(style);
        g.setStroke(str);

    }

    public static void applyStyleFill(SvgObject target, Graphics2D g) {

        Style style = target.getStyle();
        float opacity = getOpacity(style, "fill-opacity");

        String colStr = style.getProperty("fill");

        if (colStr == null) {
            g.setColor(Color.BLACK);

        } else if (colStr.startsWith("url(")) {

            SvgDefinition def = target.resolveUrl(colStr.substring(4, colStr.length() - 1));

            if (def != null) {
                LOG.debug("Fill with " + def);

                if (def instanceof Paintable) {
                    Paintable paintable = (Paintable) def;

                    g.setPaint(paintable.getPaint());

                }

            } else {

                LOG.warn("Unable to fill with color " + colStr
                        + " not resolved (or not yet implemented)");
            }

        } else {
            g.setColor(StyleUtils.getColorFromHexString(colStr, opacity));
        }

    }

    public static float getOpacity(Style style, String propertyName) {

        String opacityStr = style.getProperty(propertyName);

        float opacity = 1.0f;
        if (opacityStr != null) {
            opacity = Float.parseFloat(opacityStr);
        }

        return opacity;
    }

}
