package algorithm;

import java.util.ArrayList;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

public class Traverse implements NodeVisitor {
	
	ArrayList<Node> orderedList = new ArrayList<Node>();
	




	public Traverse(){
		
	}

	public void head(Node _node, int _depth) {
	
		
	}

	public void tail(Node _node, int _depth) {
		
//		if(!(_node instanceof TextNode)){
		
		orderedList.add(_node.clone());

		
//		System.out.println(_node.nodeName()+" depth "+_depth);
//		}
		
		
	}
	
	
	
	
	
	
	public ArrayList<Node> getOrderedList() {
		return orderedList;
	}

}
