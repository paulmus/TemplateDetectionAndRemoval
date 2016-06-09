package Measuring;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import importFiles.DomCrawler;

/**
 * @author paulmuschiol
 * process the algortihms
 * and calc the results
 */
public class Measure {
	
	/**
	 * crawling helper for the dom trees
	 */
	DomCrawler dc = null;
	



	/**
	 * set of template words
	 */
	Set<String> template = null;
	
	/**
	 * set of template words of manually detected template
	 */
	Set<String> manualTemplate = null;
	
	
	/**
	 * process template detection and removal of the given domain
	 * measure the result
	 * @param _foldername the folder to calc
	 */
	public Measure(String _foldername){
		
		System.out.println("### "+_foldername);
		
		dc = new DomCrawler(_foldername);
		dc.processFolder();
		
		template = new HashSet<String>();
		getTextDescendants(dc.getTemplateTree(),template);
		
		if(dc.getManualTemplateTree() != null){
			manualTemplate = new HashSet<String>();
			getTextDescendants(dc.getManualTemplateTree().getDocumentElement(),manualTemplate);
			
//			for(String a : template){
//				System.out.print("\""+a+"\"");
//			}
//			System.out.println();
//			for(String a : manualTemplate){
//				System.out.print("\""+a+"\"");
//			}
//			System.out.println();
			
	
			double p = precision(manualTemplate,template);
			double r = recall(manualTemplate,template);
			
			System.out.println("Precision "+p);
			System.out.println("Recall "+r);
			System.out.println("F-measure "+fmeasure(p,r));
			System.out.println();
		}
		

		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	/**
	 * calculate recall
	 * @param _visually
	 * @param _detected
	 * @return
	 */
	private double recall(Set<String> _visually, Set<String> _detected){
		Set<String> st = new HashSet<String>(); //intersection
		st.addAll(_visually);
		st.retainAll(_detected);
		
		if(_visually.size() == 0){
			return 1.0;
		}
		
		
		return ((double)st.size()/_visually.size());
	}
	
	/**
	 * calculate precision
	 * @param _visually
	 * @param _detected
	 * @return
	 */
	private double precision(Set<String> _visually, Set<String> _detected){
		Set<String> st = new HashSet<String>(); //intersection
		st.addAll(_visually);
		st.retainAll(_detected);
		
		if(_detected.size() == 0){
			return 1.0;
		}
		
		
		
		return ((double)st.size() / _detected.size());
	}
	
	/**
	 * calculate f measure
	 * @param _precision
	 * @param _recall
	 * @return
	 */
	private double fmeasure(double _precision, double _recall){
		return (2*_recall*_precision)/(_recall+_precision);
	}
	
	/**
	 * preorder traversal of the tree
	 * add all words from textnodes to the set
	 * remove any specical characters
	 * @param _node a root node to check from
	 * @param _words the set containing the words
	 */
	private static void getTextDescendants(Node _node, Set<String> _words){
		
		NodeList a = _node.getChildNodes();
		
		for( int u=0; u<a.getLength(); u++){
			
			getTextDescendants(a.item(u), _words);
			
			if(a.item(u).getNodeType() == Node.TEXT_NODE){
				String text = a.item(u).getTextContent().trim();
				
				text = text.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", " ");
				
				String[] singleWords = text.split(" ");
				
				for( String h : singleWords ){
					if(!h.isEmpty()){
						
						
						_words.add(h.toLowerCase());
					
					
					
					}
				}
				

			}
			
			
			
		}

		
	}
	


	public static void main(String[] args){
		new Measure("vacu-form.de");
	}
	
	public DomCrawler getDc() {
		return dc;
	}


}
