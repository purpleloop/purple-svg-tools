package io.github.purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.Stack;

import io.github.purpleloop.tools.svg.tools.StyleUtils;

public class Ellipse extends SvgObject {

	private double cx;
	private double cy;
	private double rx;
	private double ry;

	public Ellipse(String id, double cx, double cy, double rx, double ry) {
		super(id);
		this.cx=cx;
		this.cy=cy;
		this.rx=rx;
		this.ry=ry;
	}

	@Override
	public void render(Graphics2D g, Stack<Transformation> trans) {
		StyleUtils.applyStyleFill(this, g);		
		g.fillOval((int) (cx-rx),(int) (cy-ry),(int) (2*rx),(int) (2*ry));
		StyleUtils.applyStyleDraw(this, g);		
		g.drawOval((int) (cx-rx),(int) (cy-ry),(int) (2*rx),(int) (2*ry));

	}

	@Override
	public SvgObject selectIn(int x, int y) {
		// TODO Select in ellipse
		return null;
	}
	
	@Override
	public void move(int dx, int dy) {
		this.cx=this.cx + dx;		
		this.cy=this.cy + dy;				
	}

}
