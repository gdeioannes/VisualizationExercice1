package infovis.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.awt.Shape;

public class Model {
	List<Shape> list = new ArrayList<Shape>();
	
	public void addShape(Shape ge){
		list.add(ge);
		//Sort by layer parameter uses Interface in abstract class
		Collections.sort(list,Collections.reverseOrder());	
	}
	
	public void removeShape(Shape s){
		list.remove(s);
	}
	public Iterator<Shape> getIterator(){
		return list.iterator();
	}
	
	public ListIterator<Shape> getListIterator() {
		return list.listIterator(list.size());
	}

}
