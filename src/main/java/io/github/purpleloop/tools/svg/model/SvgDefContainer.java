package io.github.purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SvgDefContainer extends SvgObject {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(SvgDefContainer.class);
    
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

        LOG.debug("Reference found : "+url);

        String ref = url.substring(1);
        

        for (SvgDefinition def : definitions) {
            if (def.getId().equals(ref)) {

                LOG.debug("Matching definition found for reference :"+def);
                return def;
            }
        }
        
        return null;
        
    }

   
    
}
