package purpleloop.tools.svg.model;

import java.awt.geom.AffineTransform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Matrix implements Transformation {
    public static final Log LOG = LogFactory.getLog(TransformationUtils.class);

    private AffineTransform trans;
    
    public Matrix(double[] values) {
                
        trans = new AffineTransform(values[0],
                values[1],
                values[2],
                values[3], 
                values[4],
                values[5]);
        
    }

    public Matrix() {

        trans = new AffineTransform();
    }

    public Matrix(AffineTransform transform) {

        trans = transform;
    }
    
    @Override
    public AffineTransform getTransformation() {
        return trans;
    }

    @Override
    public String toSvg() {
        

        StringBuffer sb = new StringBuffer();
        sb.append("matrix(");
        
        double [] flatmatrix = new double[6];

        trans.getMatrix(flatmatrix);
        
        for (int i=0; i<6; i++) {
            if (i>0) {
                sb.append(",");
            }
            sb.append(flatmatrix[i]);

        }

        sb.append(")");
        
        
        
        return sb.toString();
    }

}
