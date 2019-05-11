package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class View extends JPanel {
	private Model model = null;
	
	int initCounter=0;
	int drawCounter=initCounter;
	
	int drawSpace=0;
	int lineSpace=5;
	int borderOffset=50;
	int spaceCounter=borderOffset;
	Font font =  new Font("Helvetic", Font.PLAIN, 6);
	Color whiteColor= Color.WHITE;
	Color blackColor= Color.BLACK;
	Color redColor = Color.RED;
	int saveLastPosX=-1;
	int saveLastPosY=-1;
	private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0);
	private Set<String> nameList= new HashSet<String>();
	boolean redLineFlag=false;
	
	//Values for data drawing
	double min, max;
	
	public Rectangle2D getMarkerRectangle() {
		return markerRectangle;			
	}
	
	public Set<String> getSelectedList() {
		return nameList;			
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		
		
		//Draw white Background
		g2D.setStroke(new BasicStroke(0));
		Rectangle2D backgroundRect= new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
		
		g2D.setColor(whiteColor);
		g2D.fill(backgroundRect);
		g2D.draw(backgroundRect);
		
		g2D.setColor(blackColor);
		drawSpace=this.getWidth()-borderOffset*2;
		lineSpace=drawSpace/model.getLabels().size();
		
		for(String label : model.getLabels()) {
			
			int linePosX=spaceCounter+lineSpace*drawCounter+borderOffset/2;
			g2D.drawLine(linePosX, borderOffset, linePosX, this.getHeight()-borderOffset);
			
			g2D.setFont(font);
			g2D.drawString(label,linePosX-borderOffset/2,borderOffset/2);
			
			drawCounter++;
		}
		
		for(int row=0;row<model.getList().size();row++) {
			for(int col=0;col<model.getList().get(row).getValues().length;col++) {
				
				int linePosX=spaceCounter+lineSpace*col+borderOffset/2;
				
				min=model.getRanges().get(col).getMin();
				max=model.getRanges().get(col).getMax();
				
				double value=model.getList().get(row).getValue(col);
				
				double smallPosY=((this.getHeight()-borderOffset*2)*(value-min)/(max-min))+borderOffset;
				
				//Draw Small Rectangle
				g2D.drawRect(linePosX, (int)smallPosY, 2, 2);
				
				//DrawText
				if(col==0) {
					g2D.drawString(model.getList().get(row).getLabel(),0+borderOffset/2,(int)smallPosY);
				}
				
				
				if(linePosX>markerRectangle.getX() && linePosX<markerRectangle.getX()+markerRectangle.getWidth() &&
						smallPosY>markerRectangle.getY() && smallPosY<markerRectangle.getY()+markerRectangle.getHeight() 
						|| nameList.contains(model.getList().get(row).getLabel())) {
					//Store the point in a List and draw red
					nameList.add(model.getList().get(row).getLabel());
					g2D.setStroke(new BasicStroke(2));
					g2D.setColor(redColor);
					redLineFlag=true;
				}else {
					g2D.setColor(blackColor);
				}
				
				if(saveLastPosX>0) {
					//Draw line
					
					g2D.drawLine(saveLastPosX, saveLastPosY, linePosX, (int)smallPosY);
					g2D.setStroke(new BasicStroke(0));
				}
				//Save Position to draw Lines
				saveLastPosX=(int)linePosX;
				saveLastPosY=(int)smallPosY;
				g2D.setColor(blackColor);
				//Pick Data points
				
			}
			redLineFlag=false;
			saveLastPosX=-1;
		}
		
		//Draw Marker
		g2D.setColor(redColor);
		g2D.draw(markerRectangle);
		g2D.setColor(blackColor);

		drawCounter=initCounter;
		
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
}
