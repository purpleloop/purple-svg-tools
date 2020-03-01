package purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SvgDefContainer extends SvgObject {

    /** Class logger. */
    private static final Logger logger = LogManager.getLogger(SvgDefContainer.class);

    
    private List<SvgDefinition> definitions;

    public SvgDefContainer(String id){
        super(id);
        definitions = new ArrayList<SvgDefinition>();
    }
    
    public void add(SvgDefinition definition) {
        definitions.add(definition);        
    }

    @Override
    public void render(Graphics2D g, Stack<Transformation> trans) {}

    @Override
    public SvgObject selectIn(int x, int y) {
        return null;
    }

    @Override
    public void move(int dx, int dy) {}

    @Override
    public SvgDefinition resolveUrl(String url) {

        logger.debug("Reference found : "+url);

        String ref = url.substring(1);
        

        for (SvgDefinition def : definitions) {
            if (def.getId().equals(ref)) {

                logger.debug("Matching definition found for reference :"+def);
                return def;
            }
        }
        
        return null;
        
    }

   
    
}
