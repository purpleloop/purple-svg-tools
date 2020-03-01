package purpleloop.tools.svg.model;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import purpleloop.tools.svg.tools.Paintable;

public class LinearGradientWithStops extends SvgAbstractObject implements SvgDefinition, Paintable, LinearGradient {

    
    public static final Log LOG = LogFactory.getLog(LinearGradientWithStops.class); 
    
    private List<GradientStop> stops;

    private Color[] colors;

    private float[] fractions;
    
    public LinearGradientWithStops(String id){
        super(id);
               
        stops = new ArrayList<GradientStop>();
    }

    public void addStop(GradientStop stopElement) {
        stops.add(stopElement);
        
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        
        sb.append("Linear gradient ("+getId()+")");

        return sb.toString();
    }

    @Override
    public Paint getPaint() {
        

        Point p1 = new Point(50,50);
        Point p2 = new Point(100,100);     
        return getPaint (p1,p2);
        
    }

    protected Paint getPaint(Point p1, Point p2) {

        if (stops.size()<2) {
            LOG.warn("Linear gradient "+getId()+" cannot be painted -> grayed");
            return Color.GRAY; 
        }
           

        computeColorsAndFractions();
        
        
        LinearGradientPaint linearGradientPaint = new LinearGradientPaint(p1, p2, fractions, colors);

        
        return linearGradientPaint;
    }

    private void computeColorsAndFractions() {

        
        int i = 0;

        colors = new Color[stops.size()];
        fractions = new float[stops.size()];
        
        for (GradientStop stop : stops) {
            colors[i] = stop.getColor();
            fractions[i] = (float) stop.getOffset();
            
            i++;
        }
               
    }

    public Paint getRadialPaint(java.awt.Point center, double r) {


        computeColorsAndFractions();
        
        RadialGradientPaint radialGradientPaint = new RadialGradientPaint(center, (float) r, fractions, colors);
        
        
        return radialGradientPaint;
    }

    
}
