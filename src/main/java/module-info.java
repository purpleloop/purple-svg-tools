module io.guithub.purpleloop.svgtools {
	
    exports io.github.purpleloop.tools.svg.model;
    exports io.github.purpleloop.tools.svg.gui;
    exports io.github.purpleloop.tools.svg.tools;
    
    requires io.github.purpleloop.commons;
    requires io.github.purpleloop.commons.swing;
    requires transitive java.desktop;
	requires transitive commons.logging;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires xmlwriter;
    requires org.apache.logging.log4j.core;
	
}