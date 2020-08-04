package io.github.purpleloop.tools.svg.model;

import java.util.ArrayList;
import java.util.List;

public class SvgTags {

	public static final String ELT_SVG = "svg";

    public static final Object ELT_DEFS = "defs";
    public static final String ELT_MARKER = "marker";
    public static final String ELT_LINEAR_GRADIENT = "linearGradient";
    public static final String ELT_RADIAL_GRADIENT = "radialGradient";
	
	
	public static final String RECTANGLE = "rect";
	public static final String PATH = "path";
	public static final String FLOW_ROOT = "flowRoot";
	public static final Object FLOW_PARA = "flowPara";
	public static final Object FLOW_REGION = "flowRegion";
	public static final Object GROUP = "g";
	
	// NYI
	public static final Object CIRCLE = "circle";
	public static final Object ELLIPSE = "ellipse";
	public static final Object LINE = "line";
	public static final Object POLYGON = "polygon";
	public static final Object POLYLINE = "polyline";	
	
	// Attributes
	public static final String ID = "id";
	public static final String PATH_STRING = "d";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String X = "x";
	public static final String Y = "y";
    public static final String X1 = "x1";
    public static final String Y1 = "y1";
    public static final String X2 = "x2";
    public static final String Y2 = "y2";

	public static final String CX = "cx";
	public static final String CY = "cy";
	public static final String RADIUS = "r";

	/** D�calage (gradients). */
    public static final String ATT_OFFSET = "offset";
	public static final String ATT_STYLE = "style";
	
    public static final String ATT_XLINK_REFERENCE = "xlink:href";


	public static final String RX = "rx";
	public static final String RY = "ry";
    public static final String FX = "fx";
    public static final String FY = "fy";
    public static final String R = "r";

	public static final String INKSCAPE_LABEL = "inkscape:label";


    public static final String GRADIENT_TRANSFORMATION = "gradientTransform";


    public static final String ATT_TRANSFORMATION = "transform";

    public static final String ELT_POLYGON = "polygon";


    public static final String ELT_IMAGE = "image";

    public static final String POINTS = "points";


    public static final String VIEW_BOX = "viewBox";


	private static List<String> ignoreList;
	
	static {
		ignoreList = new ArrayList<String>();
		ignoreList.add("metadata");
		
		// Inscape / Sodipodi metadata for named view (useful only for editor)
		ignoreList.add("sodipodi:namedview");				
	};
	
	public static List<String> getIgnoreList() {
		return ignoreList ;
	}

    public static void updateIgnoreList(String name) {
        ignoreList.add(name);
        
    }


}
