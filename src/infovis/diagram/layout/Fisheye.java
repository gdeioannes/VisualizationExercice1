package infovis.diagram.layout;

import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Vertex;

public class Fisheye implements Layout{

	public View view;
	double d= 1;
	
	public void setMouseCoords(int mouseX, int mouseY, View view) {
		for(Vertex vertex : view.getModel().getVertices()) {
			
			double PfocusX=mouseX;
			double PfocusY=mouseY;
			
			double DnormX=Dnorm(PfocusX,vertex.getX());
			double DnormY=Dnorm(PfocusY,vertex.getY());
			
			double DmaxX=Dmax(view.getWidth(),PfocusX,vertex.getX());
			double DmaxY=Dmax(view.getHeight(),PfocusY,vertex.getY());
			
			double PfishX=G(DnormX/DmaxX)*DmaxX+PfocusX;
			double PfishY=G(DnormY/DmaxY)*DmaxY+PfocusY;
			
			vertex.transformFishX=PfishX;
			vertex.transformFishY=PfishY;
			
			double QnormX= vertex.getX()+(vertex.getWidth()/2);
			double QnormY= vertex.getY()+(vertex.getHeight()/2);
	
			double QfishX= G(Dnorm(PfocusX,QnormX)/Dmax(view.getWidth() ,PfocusX,QnormX))*Dmax(view.getWidth() ,PfocusX,QnormX)+PfocusX;
			double QfishY= G(Dnorm(PfocusY,QnormY)/Dmax(view.getHeight(),PfocusY,QnormY))*Dmax(view.getHeight(),PfocusY,QnormY)+PfocusY;
			double Sgeom =2*Math.min(Math.abs(QfishX-PfishX),Math.abs(QfishY-PfishY));
			vertex.vertexRadiusTranform=Sgeom;
		}
	}
	
	double G(double val) {
		return ((d+1)*val)/(d*val+1);
	}
	
	double Dnorm(double Pfocus, double ver) {
		return ver-Pfocus;
	}
	
	double Dmax(double screen,double Pfocus,double ver) {
		if(ver>Pfocus) {
			return screen-Pfocus;
		}else {
			return 0-Pfocus;
		}
	}

	public Model transform(Model model, View view) {
		// TODO Auto-generated method stub
		return null;
	}
}
