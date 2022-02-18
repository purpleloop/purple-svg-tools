package io.github.purpleloop.tools.svg.model;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** A class used to parse SVG paths. */
public class PathParser {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(PathParser.class);

    /** The path parts. */
    private String[] partsArray;

    /** The current path index. */
    private int partIndex = 0;

    /** Creates a path parser from a path description. 
     * @param pathDescription the string containing the path expression.
     * */
    public PathParser(String pathDescription) {

        LOG.debug("Path expression to analyse :" + pathDescription);

        partsArray = pathDescription.split("[\\s,]");

        Pattern dp;
    }

    public boolean hasNext() {
        return partIndex < partsArray.length;
    }
    
    public String peekNextPart() {
        return partsArray[partIndex];
    }
    
    public String nextPart() {
        return partsArray[partIndex++];
    }

    public int getIndex() {
        return partIndex;
    }

    public String currentPart() {
        return partsArray[partIndex];
    }

    public boolean nextIsNumber() {

        return hasNext()
                && (currentPart().matches("[\\-0-9e.]+"));
    }

}
