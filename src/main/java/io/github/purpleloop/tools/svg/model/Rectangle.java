package io.github.purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Stack;

import com.generationjava.io.xml.XmlWriter;

import io.github.purpleloop.tools.svg.tools.StyleUtils;

public class Rectangle extends SvgObject {

    private double x;
    private double y;
    private double width;
    private double height;

    public Rectangle(String id, double x, double y, double width, double height) {

        super(id);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Rectangle (" + x + "," + y + "," + width + "," + height + ")";
    }

    public void writeXMLShape(XmlWriter xmlWriter) throws IOException {
        xmlWriter.writeEntity("rect");
        xmlWriter.writeAttribute("id", getId());
        xmlWriter.writeAttribute("x", x);
        xmlWriter.writeAttribute("y", y);
        xmlWriter.writeAttribute("width", width);
        xmlWriter.writeAttribute("height", height);
        xmlWriter.writeAttribute("style", "fill:#FFFFFF;stroke:#000000;stroke-width:1");
        xmlWriter.endEntity();
    }

    @Override
    public void render(Graphics2D g, Stack<Transformation> transformationStack) {

        preTransform(transformationStack, g);
        StyleUtils.applyStyleFill(this, g);

        g.fillRect((int) x, (int) y, (int) width, (int) height);
        StyleUtils.applyStyleDraw(this, g);
        g.drawRect((int) x, (int) y, (int) width, (int) height);

        postTransform(g);

    }

    @Override
    public SvgObject selectIn(int xx, int yy) {

        boolean in = (xx >= x) && (xx <= x + width) && (yy >= y) && (yy <= y + height);
        return (in) ? this : null;
    }

    @Override
    public void move(int dx, int dy) {
        this.x = this.x + dx;
        this.y = this.y + dy;
    }

}
