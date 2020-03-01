package purpleloop.tools.svg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TestPanel extends JPanel implements ActionListener {

	/** Serialization tag. */
	private static final long serialVersionUID = 2218351047252087112L;
	
	private GeneralPath p1;
	private GeneralPath p2;

	Timer timer;
	private GeneralPath p3;
	
	public TestPanel() {
		super();
		setPreferredSize(new Dimension(500,500));			

		p1 = new GeneralPath();
		p1.moveTo(100,100);
		p1.curveTo(85,150,180,180,200,200);
		p1.lineTo(250,50);
		p1.lineTo(100,100);
		
		p2 = new GeneralPath();
		p2.moveTo(100,100);
		p2.curveTo(25,125,25,175,100,200);
		p2.curveTo(125,275,175,275,200,200);
		p2.curveTo(275,175,275,125,200,100);
		p2.curveTo(175,25,125,25,100,100);
		
		p3= (GeneralPath) p2.clone();
		AffineTransform at= new AffineTransform();
		at.translate(200,0);
		p3.transform(at);
		
		timer = new Timer(50,this);
		timer.start();
		
	}

	
	@Override
	public void paint(Graphics g){
		
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		

		g.setColor(Color.BLACK);
		g2.draw(p1);
		g.setColor(Color.PINK);						
		g2.fill(p1);

		g.setColor(Color.BLACK);
		g2.draw(p2);
		g.setColor(new Color(0,0,1.0f,0.75f));		
		
		

		

		Paint grad1 = new GradientPaint(5,5,Color.RED,250,250,Color.BLUE);

	
		
		
		g2.setPaint(grad1);
		g2.fill(p2);
		
		g.setColor(Color.BLACK);
		g2.draw(p3);
		g.setColor(Color.RED);		
		Paint grad2 = new GradientPaint(205,5,Color.YELLOW,350,250,Color.GREEN);
		g2.setPaint(grad2);				
		g2.fill(p3);
//		g2.draw(p);
		
		
		
		
		/*
		if (svgDoc!=null) {
			
			svgDoc.render(g2);
			
		} */
	}

	public void actionPerformed(ActionEvent e) {

		AffineTransform at = new AffineTransform();
		at.rotate(0.1,150,150);
		
		p2.transform(at);	
		
		AffineTransform at2 = new AffineTransform();
		at2.rotate(-0.1,350,150);
		p3.transform(at2);	
		repaint();
		
	}

}
