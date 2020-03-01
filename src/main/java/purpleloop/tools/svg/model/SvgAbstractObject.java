package purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.Stack;

public class SvgAbstractObject extends SvgObject {

    public SvgAbstractObject(String id) {
        super(id);
    }

    @Override
    public void render(Graphics2D g, Stack<Transformation> trans) {}

    @Override
    public SvgObject selectIn(int x, int y) {
        return null;
    }

    @Override
    public void move(int dx, int dy) {}

}
