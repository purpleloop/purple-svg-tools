package purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.Stack;

public class FlowRoot extends SvgContainer  {

	private Transformation transformation;

    public FlowRoot(String id) {
		super(id);
	}

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    @Override
    public void render(Graphics2D g, Stack<Transformation> trans) {
        super.render(g, trans);
    }

}
