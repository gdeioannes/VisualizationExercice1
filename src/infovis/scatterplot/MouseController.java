package infovis.scatterplot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;
	private boolean dragFlag=false;
	int posx=0,posy=0;

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		//Iterator<Data> iter = model.iterator();
		//view.getMarkerRectangle().setRect(x,y,w,h);
		//view.repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		//Release Selection
		dragFlag=false;
	}

	public void mouseDragged(MouseEvent e) {
		if(!dragFlag) {
			posx=e.getX();
			posy=e.getY();
			view.getSelectedList().clear();
			dragFlag=true;
		}
		view.getMarkerRectangle().setRect(posx,posy,e.getX()-posx,e.getY()-posy);
		view.repaint();
		
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void setModel(Model model) {
		this.model  = model;	
	}

	public void setView(View view) {
		this.view  = view;
	}

}
