package io.github.purpleloop.tools.svg.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransformationUtils {
    public static final Log LOG = LogFactory.getLog(TransformationUtils.class);

    public static Transformation parse(String str) {

        LOG.debug("Parsing transformation "+str);
        
        Transformation trans = null;

        if (str.startsWith("matrix(")) {
            
            String coordsStr = str.substring(7, str.length()-1);

            String[] coords = coordsStr.split(",");
            
            if (coords.length!=6) {
                LOG.warn("Number of values must be 6 in matrices -> aborted");
            } else {

                double[] values = new double[6];
                for (int i=0; i<6; i++) {
                    values[i] = Double.parseDouble(coords[i]);
                }


                Matrix mat = new Matrix(values);
                
                trans = mat;
            }
            

        } else {

            LOG.warn("Unsupported transformation '" + str+"'");
        }

        return trans;
    }

}
