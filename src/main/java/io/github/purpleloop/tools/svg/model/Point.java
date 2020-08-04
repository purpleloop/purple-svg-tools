package io.github.purpleloop.tools.svg.model;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        this(other.x, other.y);
    }

    public static Point parseCoords(PathParser pathParser) {

        String xStr = pathParser.nextPart();
        String yStr = pathParser.nextPart();
        double x = Double.parseDouble(xStr);
        double y = Double.parseDouble(yStr);

        return new Point(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public void setLocation(Point other) {
        this.x = other.x;
        this.y = other.y;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;

    }

    public java.awt.Point toAwtPoint() {
        return new java.awt.Point((int) x, (int) y);
    }

    public double distanceTo(Point targetPoint) {

        double dx = targetPoint.getX() - this.x;
        double dy = targetPoint.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
