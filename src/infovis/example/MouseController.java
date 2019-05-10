package infovis.example;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	
	public MouseController() {
		
	}
	
	public void mouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON2){
			
			System.out.println("MidleClikc");
		}
		
		if (e.getButton() == MouseEvent.BUTTON3){
			
			System.out.println("Left Clikc");
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1){
			
		
		}
	}

	public void mouseReleased(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1){
			view.stopDraging();
		}
		if (e.getButton() == MouseEvent.BUTTON3){
			view.stopScaling();
		}
	}

	public void mouseDragged(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1){
			view.moveBigImage(e);
		}
		
		if (e.getButton() == MouseEvent.BUTTON3){
			view.scaleBigImage(e);
		}
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
