package purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SvgContainer extends SvgObject {
	
	private List<SvgObject> objects;
	
	protected SvgContainer(String id){
		super(id);
		this.objects = new ArrayList<SvgObject>();
	}

	public void addObject(SvgObject object) {
		this.objects.add(object);		
	}
	

	public void render(Graphics2D g, Stack<Transformation> trans) {

		for (SvgObject o :objects) {
			o.render(g, trans);
		}
	}


	public SvgObject selectIn(int x, int y) {

		SvgObject selected = null;
		
		for (SvgObject o :objects) {
			selected = o.selectIn(x, y);
			if (selected!=null) {
				return selected;
			}
		}
		return null;
	}
	
	
	@Override
	public void move(int dx, int dy) {
		for (SvgObject o :objects) {
			o.move(dx,dy);
		}
	}

	@Override
	public void registerIds(Map<String, SvgObject> mapId) {
		super.registerIds(mapId);
		
		for (SvgObject o :objects) {
			o.registerIds(mapId);
		}
		
	}
	

    public SvgDefinition resolveUrl(String url) {
        
        SvgDefinition def = null;
        
        for (SvgObject o :objects) {
            
            if (o instanceof SvgDefContainer) {
                SvgDefContainer defContainer = (SvgDefContainer) o;                                       
                
                def = defContainer.resolveUrl(url);
                
                if (def!= null) {
                    return def;
                }
            }
                        
        }        
        
        return super.resolveUrl(url);
    }
		
}
