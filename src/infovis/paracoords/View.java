package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
	Font font =  new Font("Helvetic", Font.PLAIN, 6);
	Color whiteColor= Color.WHITE;
	Color blackColor= Color.BLACK;
	Color redColor = Color.RED;
	Color pinkColor = Color.PINK;
	int saveLastPosX=-1;
	int saveLastPosY=-1;
	private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0);
	private Set<String> nameList= new HashSet<String>();
	boolean redLineFlag=false;
	boolean changeDataFlag=false;
	//Values for data drawing
	double min, max;
	int pickLabel=-1;
	Point clickPoint=new Point();
	ArrayList<Integer> drawAxisOrder= new ArrayList<Integer>();
	public boolean flagDragAxis=false, flagChangeLabel=false;;
	int clickPosX=0,clickPosY=0;
	int dragMousePosX;
	public String pickedLabel="";
	
	View(){
		
	}
	
	public Point getPoint() {
		return clickPoint;			
	}
	
	public Rectangle2D getMarkerRectangle() {
		return markerRectangle;			
	}
	
	public Set<String> getSelectedList() {
		return nameList;			
	}
	
	public void changeData(int exchangeLabel) {
		changeDataFlag=true;
		String saveData=model.getLabels().get(exchangeLabel);
		model.getLabels().set(exchangeLabel, model.getLabels().get(pickLabel));
		model.getLabels().set(pickLabel, saveData);
		int saveIndex=drawAxisOrder.get(pickLabel);
		drawAxisOrder.set(pickLabel,drawAxisOrder.get(exchangeLabel));
		drawAxisOrder.set(exchangeLabel,saveIndex);
		
		pickLabel=-1;
		
		this.repaint();
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
		
		//Draw Lines and Labels
		for(String label : model.getLabels()) {
			
			int linePosX=borderOffset+lineSpace*drawCounter+borderOffset/2;
			g2D.drawLine(linePosX, borderOffset, linePosX, this.getHeight()-borderOffset);
			g2D.setFont(font);
			
			//DragAxistFunction
			g2D.setColor(pinkColor);
			g2D.drawRect(linePosX-lineSpace/2,0, lineSpace,borderOffset);
			
			if(clickPosX >linePosX-lineSpace/2 && clickPosX<linePosX+lineSpace/2 && clickPosY<borderOffset && !flagDragAxis && !flagChangeLabel) {
				flagDragAxis=true;
				flagChangeLabel=true;
				pickedLabel=label;
				pickLabel=drawCounter;
				System.out.println("Pick Label:"+label);
			}
			
			//DRAG label
			if(flagDragAxis && pickedLabel==label) {
				g2D.setColor(blackColor);
				g2D.drawString(label,dragMousePosX,borderOffset/2);
			}
			
			//DROP Label
			if(pickedLabel!="" && !flagDragAxis) {
				
				System.out.println("Drop Label");
				System.out.println("X:"+clickPosX+" "+(linePosX-lineSpace/2));
				if(clickPosX>linePosX-lineSpace/2 && clickPosX<linePosX+lineSpace/2) {
					System.out.println("Change Label:"+label + " With:"+pickedLabel);
					pickedLabel="";
					flagChangeLabel=false;
					changeData(drawCounter);
					break;
				}
			}
			
			//Draw Text Labels
			if(pickedLabel!=label) {
				g2D.setColor(blackColor);
				g2D.drawString(label,linePosX-borderOffset/2,borderOffset/2);
			}
			
			if(clickPoint.x>linePosX-(lineSpace/2) && clickPoint.x<linePosX+lineSpace/2) {
				if(pickLabel==-1 && !changeDataFlag) {
					//System.out.println("Pick label");
					pickLabel=drawCounter;
				}else if(pickLabel!=-1){
					//System.out.println("Change label");
					changeData(drawCounter);
				}else {
					changeDataFlag=false;
				}	
			}
			drawCounter++;
		}
		
		for(int row=0;row<model.getList().size();row++) {
			for(int col=0;col<model.getList().get(row).getValues().length;col++) {
				
				int linePosX=borderOffset+lineSpace*col+borderOffset/2;
				
				//BigChange use drawAxisOrder to change value between axis with out altering the data of the model
				min=model.getRanges().get(drawAxisOrder.get(col)).getMin();
				max=model.getRanges().get(drawAxisOrder.get(col)).getMax();
				
				double value=model.getList().get(row).getValue(drawAxisOrder.get(col));
				
				double smallPosY=((this.getHeight()-borderOffset*2)*(value-min)/(max-min))+borderOffset;
				
				//Selected input Rectangle
				if( checkMarkerRectangle(linePosX,(int)smallPosY)|| nameList.contains(model.getList().get(row).getLabel())) {
					//Store the point in a List and draw red
					nameList.add(model.getList().get(row).getLabel());
					g2D.setStroke(new BasicStroke(1));
					g2D.setColor(redColor);
					redLineFlag=true;
					g2D.setStroke(new BasicStroke(0));
				}else {
					g2D.setColor(blackColor);
				}
				
				if(nameList.size()==0 || nameList.contains(model.getList().get(row).getLabel())) {
					//Draw Small Rectangle
					g2D.drawRect(linePosX, (int)smallPosY, 2, 2);
					
					//DrawText
					if(col==0) {
						
						g2D.drawString(model.getList().get(row).getLabel(),0+borderOffset/2,(int)smallPosY);
					}
					
					if(saveLastPosX>0) {
						//Draw line
						g2D.drawLine(saveLastPosX, saveLastPosY, linePosX, (int)smallPosY);
						g2D.setStroke(new BasicStroke(0));
					}
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
		
		if(markerRectangle.getCenterY()>borderOffset && !flagDragAxis) {
			g2D.setColor(redColor);
			g2D.draw(markerRectangle);
		}
		
		g2D.setColor(blackColor);

		drawCounter=initCounter;
		
	}

	boolean checkMarkerRectangle(int linePosX, int smallPosY) {
		if(linePosX>markerRectangle.getX() && linePosX<markerRectangle.getX()+markerRectangle.getWidth() &&
		smallPosY>markerRectangle.getY() && smallPosY<markerRectangle.getY()+markerRectangle.getHeight() &&
		markerRectangle.getY()>borderOffset && !flagDragAxis) {
			return true;
		}
		return false;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public Model getModel() {
		return model;
	}

	void setdrawingAxisList() {
		for(int i=0;i<model.getLabels().size();i++) {
			drawAxisOrder.add(i);
		}
	}
	
	public void setModel(Model model) {
		this.model = model;
		setdrawingAxisList();
	}
	
}
