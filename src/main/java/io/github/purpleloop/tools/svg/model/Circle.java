package io.github.purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.Stack;

import io.github.purpleloop.commons.math.GeomUtils;
import io.github.purpleloop.tools.svg.tools.StyleUtils;

public class Circle extends SvgObject {
	
	private double x;
	private double y;
	private double radius;

	public Circle(String id, double x, double y, double r) {
		super(id);
		this.x = x;
		this.y =y;
		this.radius = r;		
		
	}

	@Override
	public void render(Graphics2D g, Stack<Transformation> transformation) {
		StyleUtils.applyStyleFill(this, g);		
		g.fillOval((int) (x-radius),(int)(y-radius),(int)(radius*2.0),(int)(radius*2.0));
		StyleUtils.applyStyleDraw(this, g);		
		g.drawOval((int) (x-radius),(int)(y-radius),(int)(radius*2.0),(int)(radius*2.0));
		
	}

	@Override
	public SvgObject selectIn(int xx, int yy) {
		
		boolean in =  GeomUtils.distance(this.x,this.y,xx,yy) < this.radius;
		return (in) ? this : null;
	}

	@Override
	public void move(int dx, int dy) {
		this.x=this.x + dx;		
		this.y=this.y + dy;				
	}

}
