package infovis.scatterplot;

import infovis.gui.GUI;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Scatterplot {
	private View view;
	private Model model;
	private MouseController controller ;
	
	public JPanel getView(){
		if (view == null) generateScatterplot();
		return view;
	}

	private void generateScatterplot() {
		view = new View();
		model = new Model();
		controller = new MouseController();
		view.setModel(model);
		controller.setModel(model);
		controller.setView(view);
		view.addMouseListener(controller);
		view.addMouseMotionListener(controller);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI application = new GUI();
				
				application.setView(new Scatterplot().getView());
				application.getJFrame().setBounds(0, 0, 850, 1000);
				application.getJFrame().setVisible(true);
			}
		});
	}

}
