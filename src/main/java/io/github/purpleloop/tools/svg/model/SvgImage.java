package io.github.purpleloop.tools.svg.model;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

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

        Composite previousComposite = g.getComposite();

        String opacityStr = getStyle().getProperty("opacity");
        if (StringUtils.isNoneBlank(opacityStr)) {
            double opacity = Double.parseDouble(opacityStr);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
        }

        g.drawImage(image, x, y, null);

        g.setComposite(previousComposite);

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
