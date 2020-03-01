package purpleloop.tools.svg.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Style {

    /** Class logger. */
    private static final Logger logger = LogManager.getLogger(Style.class);

	
	public static List<String> allProperties;
	
	private Map<String,String> properties;	

	private Style parentStyle;
	
	static {
		allProperties = new ArrayList<String>();
	}

	public Style(Style parentStyle){
		this.parentStyle = parentStyle;
		this.properties = new HashMap<String,String>();
	}
	
	
	public Style() {
		this(null);
	}


	public static Style parseCss(Style parentStyle, String styleStr) {
		Style style = new Style(parentStyle);
		
		String[] properties = styleStr.split(";");
		String[] affectation;
		String propertyName;
		String valueString;
		
		try {
			for (String property : properties) {
								
				affectation = property.split(":");	
				if (affectation.length==2) {

					propertyName = affectation[0];
					valueString = affectation[1];
										
					style.setProperty(propertyName,valueString);
					logger.debug("- "+propertyName+" <- "+valueString);
					
					if (!allProperties.contains(propertyName)) {
						allProperties.add(propertyName);
					}
					
				}
				
				
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("Style analysis failed for string '"+styleStr+"'",e);
		}
		
		
		return style;
	}


	public void setProperty(String propertyName, String valueString) {
		properties.put(propertyName,valueString);		
	}
	

	public String getProperty(String propertyName) {
		
		if (properties.containsKey(propertyName)) {
			return properties.get(propertyName);
			
		} else if (parentStyle!=null) {
			return parentStyle.getProperty(propertyName);
		} else {
			return null;
		}
	}


	public static List<String> getAllProperties() {
		return allProperties;
	}
	
}
