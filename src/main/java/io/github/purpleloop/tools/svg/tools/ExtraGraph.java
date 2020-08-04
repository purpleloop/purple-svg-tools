package io.github.purpleloop.tools.svg.tools;

import java.awt.Graphics;

/** Extra graphics utilities. */
public class ExtraGraph {

    /**
     * Draw a Bezier curve.
     * 
     * @param g Graphic where to draw
     * @param x0 X location of the first control point
     * @param y0 Y location of the first control point
     * @param x1 X location of the second control point
     * @param y1 Y location of the second control point
     * @param x2 X location of the third control point
     * @param y2 Y location of the third control point
     * @param x3 X location of the fourth control point
     * @param y3 Y location of the fourth control point
     */
    public static void drawBezier(Graphics g, double x0, double y0, double x1, double y1, double x2,
            double y2, double x3, double y3) {
        drawBezier(g, x0, y0, x1, y1, x2, y2, x3, y3, 50);
    }

    /**
     * Internal recursive draw a Bezier curve (see De Casteljau Algorithm).
     * 
     * https://en.wikipedia.org/wiki/De_Casteljau%27s_algorithm
     * 
     * @param g Graphic where to draw
     * @param x0 X location of the first control point
     * @param y0 Y location of the first control point
     * @param x1 X location of the second control point
     * @param y1 Y location of the second control point
     * @param x2 X location of the third control point
     * @param y2 Y location of the third control point
     * @param x3 X location of the fourth control point
     * @param y3 Y location of the fourth control point
     * @param maxDepth maxDepth (0 for a linear draw)
     */
    private static void drawBezier(Graphics g, double x0, double y0, double x1, double y1,
            double x2, double y2, double x3, double y3, int maxDepth) {

        double xa = (x0 + x1) / 2;
        double ya = (y0 + y1) / 2;

        double xb = (x1 + x2) / 2;
        double yb = (y1 + y2) / 2;

        double xc = (x2 + x3) / 2;
        double yc = (y2 + y3) / 2;

        double xd = (xa + xb) / 2;
        double yd = (ya + yb) / 2;

        double xe = (xb + xc) / 2;
        double ye = (yb + yc) / 2;

        double xf = (xd + xe) / 2;
        double yf = (yd + ye) / 2;

        if ((maxDepth == 0) || (Math.sqrt((x0 - x3) * (x0 - x3) + (y0 - y3) * (y0 - y3)) <= 2)) {

            g.drawLine((int) x0, (int) y0, (int) xf, (int) yf);
            g.drawLine((int) xf, (int) yf, (int) x3, (int) y3);
        } else {

            drawBezier(g, x0, y0, xa, ya, xd, yd, xf, yf, maxDepth - 1);
            drawBezier(g, xf, yf, xe, ye, xc, yc, x3, y3, maxDepth - 1);
        }

    }

}
