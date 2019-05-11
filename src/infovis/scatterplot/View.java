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
	    
		Font font =  new Font("Helvetic", Font.PLAIN, 6);
		AffineTransform affineTransform = new AffineTransform();
		Font rotatedFont;
		
		Color whiteColor = Color.white;
		Color blackColor = Color.black;
		Color redColor = Color.red;
		
		//Values for data drawing
		double minW, maxW,minH,maxH;

		View(){
			affineTransform.rotate(Math.toRadians(90), 0, 0);
			rotatedFont = font.deriveFont(affineTransform);
		}
		
		public Set<String> getSelectedList() {
			return nameList;			
		}
	    
		public Rectangle2D getMarkerRectangle() {
			return markerRectangle;			
		}
		 
		@Override
		public void paint(Graphics g) {
			
			Graphics2D g2D=(Graphics2D) g;
			
			//Draw white Background
			g2D.setStroke(new BasicStroke(0));
			Rectangle2D backgroundRect= new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
			
			g2D.setColor(whiteColor);
			g2D.fill(backgroundRect);
			g2D.draw(backgroundRect);
			
			//Back to black color
			g2D.setColor(blackColor);
			
			//Draw Grid and Data
			for(int row =0;row<model.getLabels().size();row++) {
				for(int col =0;col<model.getLabels().size();col++) {
					
					//Grid
					int posX=rectW*row+rectOffset;
					int posY=rectW*col+rectOffset;
					g2D.setStroke(new BasicStroke(1));
					Rectangle2D recGrid = new Rectangle2D.Double(posX,posY,rectW,rectW);
					g2D.draw(recGrid);
					
					//Min and Max Values for data drawing
					minW=model.getRanges().get(row).getMin();
					maxW=model.getRanges().get(row).getMax();
					minH=model.getRanges().get(col).getMin();
					maxH=model.getRanges().get(col).getMax();
					
					//Data points Drawing
					for(int value=0;value<model.getList().size();value++) {
						
						double valueRow=model.getList().get(value).getValue(row);
						double valueCol=model.getList().get(value).getValue(col);

						double smallPosX=(rectW*(valueRow-minW)/(maxW-minW));
						double smallPosY=(rectW*(valueCol-minH)/(maxH-minH));
						
						//Pick Data points
						if(posX+smallPosX>markerRectangle.getX() && posX+smallPosX<markerRectangle.getX()+markerRectangle.getWidth() && 
								posY+smallPosY>markerRectangle.getY() && posY+smallPosY<markerRectangle.getY()+markerRectangle.getHeight() || nameList.contains(model.getList().get(value).getLabel())) {
							//Store the point in a List and draw red
							
							nameList.add(model.getList().get(value).getLabel());
							g2D.setStroke(new BasicStroke(2));
							g2D.setColor(redColor);
						}else {
							g2D.setStroke(new BasicStroke(1));
							g2D.setColor(blackColor);
						}
						//Draw Data point
						
						Rectangle2D recSmall = new Rectangle2D.Double(posX+smallPosX,posY+smallPosY,1,1);
						g2D.draw(recSmall);
						g2D.setColor(blackColor);
					}
				}
			}
			
			
			//DrawLabels
			//Pint Labels
			for(int i=0;i<model.getLabels().size();i++) {
				
				//Horizontal
				g2D.setFont(font);
				String label1=model.getLabels().get(i);
				g2D.drawString(label1, rectW*i+rectOffset,rectOffset/2);
				
				//Vertical Labels
				g2D.setFont(rotatedFont);
				g2D.drawString(label1, rectW/2, rectW*i+rectOffset);
			}
			
			//Draw Marker
			g2D.setColor(redColor);
			g2D.draw(markerRectangle);
			g2D.setColor(blackColor);
			
		}
		
		public void setModel(Model model) {
			this.model = model;
		}
		
}
