package io.github.purpleloop.tools.svg.model;

import java.awt.geom.AffineTransform;

public interface Transformation {

    AffineTransform getTransformation();

    String toSvg();

}
