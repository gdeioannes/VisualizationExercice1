package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class View extends JPanel {

		private static final long serialVersionUID = 1L;
		private Model model = null;
	    private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 
	    private int rectW=60; 
	    private int rectOffset=60; 
	    private Set<String> nameList= new HashSet<String>();
	    

		public Set<String> getSelectedList() {
			return nameList;			
		}
	    
		public Rectangle2D getMarkerRectangle() {
			return markerRectangle;			
		}
		 
		@Override
		public void paint(Graphics g) {
			setBounds(0, 0, 1000, 1000);
			
			Graphics2D g2D=(Graphics2D) g;
			 			
	        /*for (String l : model.getLabels()) {
				Debug.print(l);
				Debug.print(",  ");
				Debug.println("");
			}
			for (Range range : model.getRanges()) {
				Debug.print(range.toString());
				Debug.print(",  ");
				Debug.println("");
			}
			for (Data d : model.getList()) {
				Debug.print(d.toString());
				Debug.println("");
			}*/
			
			Color whiteColor = Color.white;
			Color blackColor = Color.black;
			Color redColor = Color.red;
			
			g2D.setStroke(new BasicStroke(0));
			Rectangle2D backgroundRect= new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
			
			g2D.setColor(whiteColor);
			g2D.fill(backgroundRect);
			g2D.draw(backgroundRect);
			
			g2D.setColor(blackColor);
			
			//Draw Rects
			for(int row =0;row<model.getLabels().size();row++) {
				for(int col =0;col<model.getLabels().size();col++) {
					
					int posX=rectW*row+rectOffset;
					int posY=rectW*col+rectOffset;
					g2D.setStroke(new BasicStroke(1));
					Rectangle2D recBig = new Rectangle2D.Double(posX,posY,rectW,rectW);
					double minW=model.getRanges().get(row).getMin();
					double maxW=model.getRanges().get(row).getMax();
					double minH=model.getRanges().get(col).getMin();
					double maxH=model.getRanges().get(col).getMax();
					//g2D.setColor(whiteColor);
					g2D.draw(recBig);
					for(int value=0;value<model.getList().size();value++) {
						
						double valueRow=model.getList().get(value).getValue(row);
						double valueCol=model.getList().get(value).getValue(col);

						double smallPosX=(rectW*(valueRow-minW)/(maxW-minW));
						double smallPosY=(rectW*(valueCol-minH)/(maxH-minH));
						
						g2D.setStroke(new BasicStroke(2));
						if(posX+smallPosX>markerRectangle.getX() && posX+smallPosX<markerRectangle.getX()+markerRectangle.getWidth() && 
								posY+smallPosY>markerRectangle.getY() && posY+smallPosY<markerRectangle.getY()+markerRectangle.getHeight() || nameList.contains(model.getList().get(value).getLabel())) {
							nameList.add(model.getList().get(value).getLabel());
							System.out.println(model.getList().get(value).getLabel());
							g2D.setColor(redColor);
						}else {
							g2D.setColor(blackColor);
						}
						
						Rectangle2D recSmall = new Rectangle2D.Double(posX+smallPosX,posY+smallPosY,1,1);
						g2D.draw(recSmall);
						g2D.setColor(blackColor);
					}
				}
			}
			g2D.setColor(redColor);
			g2D.draw(markerRectangle);
			
			g2D.setColor(blackColor);
			
			//DrawLabels
			Font font =  new Font("Helvetic", Font.PLAIN, 7);
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.rotate(Math.toRadians(90), 0, 0);
			Font rotatedFont = font.deriveFont(affineTransform);
			
			for(int i=0;i<model.getLabels().size();i++) {
				g2D.setFont(font);
				String label1=model.getLabels().get(i);
				String minMax=model.getRanges().get(i).getMin()+" "+model.getRanges().get(i).getMax();
				g2D.drawString(label1, rectW*i+rectOffset,rectOffset/2);
				g2D.drawString(minMax, rectW*i+rectOffset,rectOffset/2+rectOffset/4);
				g2D.setFont(rotatedFont);
				g2D.drawString(label1, rectW/2, rectW*i+rectOffset);
				g2D.drawString(minMax, rectW/4, rectW*i+rectOffset);
			}
			
		}
		
		public void setModel(Model model) {
			this.model = model;
		}
		
}
