package purpleloop.tools.svg.model;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PathParser {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(PathParser.class);

    private String[] partsArray;

    private int partIndex = 0;

    public PathParser(String pathDescription) {

        LOG.debug("Path analysis :" + pathDescription);

        partsArray = pathDescription.split("[\\s,]");

        Pattern dp;
    }

    public boolean hasNext() {
        return partIndex < partsArray.length;
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
                && (currentPart().matches("[\\-0-9.]+"));
    }

}
