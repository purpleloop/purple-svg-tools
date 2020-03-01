package purpleloop.tools.svg.model;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import purpleloop.tools.svg.tools.Paintable;

public class RadialGradientReference extends SvgAbstractObject  implements LinearGradient, Paintable {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(RadialGradientReference.class);
   
    private String reference;
    private Point center;
    private double fx;
    private double fy;
    private double r;

    public RadialGradientReference(String id, String reference, Point center, double fx, double fy, double r) {

        super(id);
        this.reference = reference;
        
        this.center = center;
        this.fx = fx;
        this.fy = fy;
        this.r = r; 
    }
    
    @Override
    public Paint getPaint() {
        
        SvgDefinition resolvedReference = resolveUrl(reference);
        
        if (resolvedReference!= null && resolvedReference instanceof LinearGradientWithStops) {

            LinearGradientWithStops linearGradientWithStops = (LinearGradientWithStops) resolvedReference;

            return linearGradientWithStops.getRadialPaint(center.toAwtPoint(),r);
        } else {
            LOG.warn("Unable to solve gradient reference "+getId()
            +" to "+reference+" as a paint object, rendering using gray color ...");
                        
            return Color.GRAY;
            
            
        }
        
    }
    

    public String toString(){

        return "Radial gradient ("+getId()+") with reference-> "+reference;
    }

}
