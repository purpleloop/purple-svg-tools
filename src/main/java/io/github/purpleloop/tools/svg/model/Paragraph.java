package io.github.purpleloop.tools.svg.model;

import java.awt.Graphics2D;
import java.util.Stack;

public class Paragraph extends SvgObject {

	private String text;

	public Paragraph(String id, String text) {
		super(id);
		this.text = text;
	}


	/** {@inheritDoc} */
	@Override
	public String toString(){
		return "Paragraph '"+text+"'";
	}


	@Override
	public void render(Graphics2D g, Stack<Transformation> trans) {
	    

	    
		g.drawString(text,20,20);
	}


	@Override
	public SvgObject selectIn(int x, int y) {

		return null;
	}
	
	@Override
	public void move(int dx, int dy) {
		
	}

	
}
