package purpleloop.tools.svg.model;

import java.awt.Color;

import purpleloop.tools.svg.tools.StyleUtils;

public class GradientStop {

    private String id;
    
    private Style style;
        
    private double offset;
    
    public GradientStop(String id, double offset) {
        this.id = id;
        this.offset = offset;
    }

    public void setStyle(Style style) {
        this.style = style;     
    }

    /**
     * @return the offset
     */
    public double getOffset() {
        return offset;
    }

    public Color getColor() {
        
        String colorStr = style.getProperty("stop-color");
        float opacity = StyleUtils.getOpacity(style, "stop-opacity");

        return StyleUtils.getColorFromHexString(colorStr,opacity);
    }
    
    
}
