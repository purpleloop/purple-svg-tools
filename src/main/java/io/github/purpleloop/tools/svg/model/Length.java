package io.github.purpleloop.tools.svg.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Length {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(Length.class);

    private static final String INTEGER_REGEXP = "[+-]?[0-9]+";

    private static final String NUMBER_REGEXP = INTEGER_REGEXP + "|[+-]?[0-9]*\\.[0-9]+";
    private static final String LENGTH_REGEXP = "(" + NUMBER_REGEXP + ")([a-z]*)";

    public static Double parse(String strValue) {

        Pattern lengthPattern = Pattern.compile(LENGTH_REGEXP);

        Matcher matcher = lengthPattern.matcher(strValue);

        if (matcher.find()) {

            String number = matcher.group(1);
            double valeur = Double.parseDouble(number);

            String unit = matcher.group(2);

            if (StringUtils.isBlank(unit) || unit.equals("px")) {

                // If you do not specify any units inside the width and height
                // attributes, the units are assumed to be pixels.

            } else if (unit.equals("mm")) {
                LOG.info("units 'mm' limit -> 300 DPI");
                valeur = valeur * 25.9 * 300.0;
            } else {

                // TODO Complete unit support
                LOG.warn("Unly 'px' are currently supported... '" + unit
                        + "' will be interpreted as px");
            }

            return valeur;
        } else {
            return 0.0;
        }
    }

}
