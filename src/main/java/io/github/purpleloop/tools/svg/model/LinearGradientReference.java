package io.github.purpleloop.tools.svg.model;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.tools.svg.tools.Paintable;

public class LinearGradientReference extends SvgAbstractObject
        implements LinearGradient, Paintable {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(LinearGradientReference.class);

    private String reference;
    private Point p1;
    private Point p2;

    public LinearGradientReference(String id, String reference, Point p1, Point p2) {

        super(id);
        this.reference = reference;

        this.p1 = p1;
        this.p2 = p2;

    }

    @Override
    public Paint getPaint() {

        SvgDefinition resolvedReference = resolveUrl(reference);

        if (resolvedReference != null && resolvedReference instanceof LinearGradientWithStops) {

            LinearGradientWithStops linearGradientWithStops = (LinearGradientWithStops) resolvedReference;

            return linearGradientWithStops.getPaint(p1.toAwtPoint(), p2.toAwtPoint());
        } else {
            LOG.warn("Unable to solve gradient reference " + getId() + " to " + reference
                    + " as a paint object, rendering using gray color ...");

            return Color.GRAY;

        }

    }

    public String toString() {

        return "Linear gradient Reference (" + getId() + ") -> " + reference;
    }

}
