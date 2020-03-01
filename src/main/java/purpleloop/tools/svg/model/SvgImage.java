package purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class SvgImage extends SvgObject {

    private BufferedImage image;
    private int x;
    private int y;

    public SvgImage(String id, BufferedImage image) {
        super(id);
        this.image = image;
    }

    @Override
    public void render(Graphics2D g, Stack<Transformation> trans) {

        g.drawImage(image, x, y, null);

    }

    @Override
    public SvgObject selectIn(int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void move(int dx, int dy) {
        // TODO Auto-generated method stub

    }

}
