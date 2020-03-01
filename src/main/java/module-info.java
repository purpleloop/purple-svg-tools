module svgtools {
	
    exports purpleloop.tools.svg.model;
    exports purpleloop.tools.svg.gui;
    exports purpleloop.tools.svg.tools;
    
    requires purpleloop.commons;
    requires transitive java.desktop;
	requires transitive commons.logging;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires xmlwriter;
    requires org.apache.logging.log4j.core;
	
}