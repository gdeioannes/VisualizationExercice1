package infovis.example;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class View extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Model model = null;
	
	float scale=1f,saveScale=1f;
	
	BufferedImage image;
	
	Rectangle bigImage=new Rectangle();
	
	int reduceRatio=5;
	
	Rectangle rect=new Rectangle();
	
	Rectangle smallImage=new Rectangle();
	
	int dragInputOffsetX=-1,dragInputOffsetY=-1;
	
	int scaleInputSaveX=-1,scaleInputSaveY=-1;
	
	boolean pickBigImage=false,pickSmallImage=false,pickRect=false;
	
	boolean scaleFlag=false,dragFlag=false,dragBigImageFlag=false,dragSmallImageFlag=false;
	
	int translateX=0,translateY=0;
	
	View(){
		String url ="img/map-of-world.jpg";
		File file = new File(url);
		try {
			 image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	List<Integer> bigImagePos= new ArrayList<Integer>();
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D)g;
		g2D.scale(1, 1);
		
		bigImage.width=(int)(this.getWidth()*scale);
		bigImage.height=(int)(this.getHeight()*scale);
		
		smallImage.width=(int)(this.getWidth())/reduceRatio;
		smallImage.height=(int)(this.getHeight())/reduceRatio;
		
		rect.width=(int)((this.getWidth()/reduceRatio)/scale);
		rect.height=(int)((this.getHeight()/reduceRatio/scale));
		
		g2D.translate(translateX, translateY);
		g2D.drawImage(image,0,0,(int)(bigImage.width),(int)(bigImage.height),0,0,image.getWidth(),image.getHeight(),null);
		
		g2D.translate(-translateX,-translateY);
		g2D.drawImage(image,smallImage.x,smallImage.y,smallImage.x+smallImage.width,smallImage.y+smallImage.height,0,0,image.getWidth(),image.getHeight(),null);
		g2D.drawRect(rect.x, rect.y, rect.width, rect.height);
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
	
	public void moveBigImage(MouseEvent e) {
		
		//SelectObject
		if(checkCollision(e, rect.x, rect.y, rect.width, rect.height) && !dragFlag) {
			pickRect=true;
			System.out.println("RectCollision");
			calculateOffSet(e, rect.x, rect.y);
		}else if(checkCollision(e, smallImage.x, smallImage.y, smallImage.width, smallImage.height) && !dragFlag){
			pickSmallImage=true;
			calculateOffSet(e, smallImage.x, smallImage.y);
			System.out.println("Over View");
		}else if(!dragFlag){
			System.out.println("Main");
			calculateOffSet(e, translateX, translateY);
			pickBigImage=true;
		}
		
		if(pickBigImage) {
			translateX=(e.getX()+dragInputOffsetX);
			translateY=(e.getY()+dragInputOffsetY);
			
			rect.x=(int)(smallImage.x+(translateX/reduceRatio)*-1/scale);
			rect.y=(int)(smallImage.y+(translateY/reduceRatio)*-1/scale);
		}
		
		if(pickSmallImage) {
			smallImage.x=(e.getX()+dragInputOffsetX);
			smallImage.y=(e.getY()+dragInputOffsetY);
			
			rect.x=(int)(smallImage.x+(((float)translateX/(float)reduceRatio)/scale)*-1);
			rect.y=(int)(smallImage.y+(((float)translateY/(float)reduceRatio)/scale)*-1);
		}
		
		if(pickRect) {
			rect.x=(e.getX()+dragInputOffsetX);
			rect.y=(e.getY()+dragInputOffsetY);
			
			translateX=(int)(((rect.x-smallImage.x)*reduceRatio)*-1*scale);
			translateY=(int)(((rect.y-smallImage.y)*reduceRatio)*-1*scale);
		}
		this.repaint();
	}
	
	public void scaleBigImage(MouseEvent e) {
		if(!scaleFlag){
			scaleInputSaveX=e.getX();
			scaleInputSaveY=e.getY();
			saveScale=scale;			
			scaleFlag=true;
		}
		
		scale=saveScale+(e.getY()-(float)scaleInputSaveY)/1000;
		centerScaling(e);
		this.repaint();
	}
	
	private void centerScaling(MouseEvent e) {
		translateX=translateX;
		translateY=translateY;
	}
	
	private void calculateOffSet(MouseEvent e,int x, int y) {
		dragInputOffsetX=x-e.getX();
		dragInputOffsetY=y-e.getY();
		dragFlag=true;
	}
	
	private void resetPick() {
		pickBigImage=false;
		pickSmallImage=false;
		pickRect=false;
	}
	
	public void stopScaling() {
		System.out.println("Stop Scale");
		scaleFlag=false;
	}
	
	private boolean checkCollision(MouseEvent e,int x,int y, int w,int h) {
		if(e.getX()>x && e.getX()<x+w  && e.getY()>y && e.getY()<y+h){
			return true;
		}
		return false;
	}
	
	public void stopDraging() {
		dragFlag=false;
		resetPick();
		System.out.println("End Drag");
	}
	
}

