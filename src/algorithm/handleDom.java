package algorithm;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

public class handleDom {
	
	Document doc = null;
	
	public handleDom(Document _doc){
		doc = _doc;
		
		Element body = doc.body();
		
		List<Node> childs = body.childNodes();
		
		Traverse tr = new Traverse();
		
		body.traverse(tr);
		

		
		
//		for( Node c : childs ){
//			
//			System.out.println(c.outerHtml());
//			c.traverse(new NodeVisitor() {
//			    public void head(Node node, int depth) {
//			        System.out.println("Entering tag: " + node.nodeName());
//			    }
//			    public void tail(Node node, int depth) {
//			        System.out.println("Exiting tag: " + node.nodeName());
//			    }
//			});
//		}
	}

}
