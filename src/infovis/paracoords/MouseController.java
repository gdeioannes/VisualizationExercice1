package infovis.paracoords;

import infovis.scatterplot.Model;

import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	Shape currentShape = null;
	
	private boolean dragFlag=false;
	int posx=0,posy=0;
	
	public void mouseClicked(MouseEvent e) {
		view.getPoint().x=e.getX();
		view.getPoint().y=e.getY();
		view.repaint();
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		//Release Selection
		dragFlag=false;
		view.flagDragAxis=false;
		view.flagChangeLabel=true;
		view.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		view.clickPosX=e.getX();
		view.clickPosY=e.getY();
		if(!dragFlag) {
			posx=e.getX();
			posy=e.getY();
			view.getSelectedList().clear();
			dragFlag=true;
		}
		view.getMarkerRectangle().setRect(posx,posy,e.getX()-posx,e.getY()-posy);
		view.dragMousePosX=e.getX();
		view.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
