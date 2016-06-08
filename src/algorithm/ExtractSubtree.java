package algorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExtractSubtree {
	
	
	
	/**
	 * first given tree/document
	 */
	Node tree1;
	
	/**
	 * second given tree/document
	 */
	Node tree2;
	
	
	
	/**
	 * is true, if there is an deleted node on tree1
	 */
	boolean hasChanged = false;
	
	
	ArrayList<Node> template_nodes = null;
	
	/**
	 * backtracking matrix, processed by RTDM-TD
	 */
	Backtracking[][] backtracking = null;


	
	
	public ExtractSubtree(Node _tree1, Node _tree2){
		
		//initialise
		tree1 = _tree1.cloneNode(true);
		tree2 = _tree2.cloneNode(true);
		
		tree1.normalize();
		tree2.normalize();
		
		

		

		
		//process treeMapping
		TreeMapping tm = new TreeMapping();
		tm.RTDM_TD(tree1, tree2);
		backtracking = tm.getBacktracking();
		
		
		
		//extract the template out of tree1 by template_nodes
		RetrieveTemplate rt = new RetrieveTemplate(backtracking);
		rt.getTemplate();
		template_nodes = rt.getRes2();
		
	

		

		
		
		
		
		
		
		
	}
	
	public Node getSmallestSubtree(){
		SmallestSubtree(template_nodes, tree1);
		
	
		

		System.out.println("***"+template_nodes.size());
		
		return tree1;
	}
	
	
	public Node getContentTree(){
		ContentTree(template_nodes, tree1);
		
		return tree1;
	}
	


	private void SmallestSubtree(ArrayList<Node> _template_nodes, Node _node) {
		
		NodeList nl = _node.getChildNodes();
		
		for(int p = 0; p<nl.getLength(); p++){
			
			if(containNode(_template_nodes, nl.item(p))){
				
				//is a template node -> go deeper
				System.out.println("§§§"+nl.item(p).getNodeName());
				
				SmallestSubtree(_template_nodes,nl.item(p));
				
			}else{
				
				hasChanged = true;
				
				//is no template node, dont go deeper
				System.out.println("???"+nl.item(p).getNodeName());
				nl.item(p).getParentNode().removeChild(nl.item(p));
				p--;
				
			}
			
			
			
		}
		
		

	}
	
	
	private boolean ContentTree(ArrayList<Node> _template_nodes, Node _node){
		
		NodeList nl = _node.getChildNodes();
		
		boolean justTemplateNodes = true;
		
		for(int p = 0; p<nl.getLength(); p++){
			
			if(containNode(_template_nodes, nl.item(p))){ 
				
				if(!nl.item(p).hasChildNodes()){ //is template node and has no children -> delete child node
					
					nl.item(p).getParentNode().removeChild(nl.item(p));
					p--;
					
				}else{ //is template node and has children -> check if the descendants are all templateNodes
					
					if(ContentTree(_template_nodes,nl.item(p))){ //in case yes -> delete all those nodes
						nl.item(p).getParentNode().removeChild(nl.item(p));
						p--;
					}else{
						justTemplateNodes = false; //-> dont delete the root
					}
					
				}
				

				
			}else{
				
				justTemplateNodes = false; //-> dont delete the root
				
			}
			
			
		}
		
		return justTemplateNodes;
		
		
	}
	





	
	private boolean containNode(ArrayList<Node> _list, Node _node){
		
		
		
		for(Node n : _list){
			
			if(_node.isEqualNode(n)){
				
				return true;
			}
		}
		
		return false;
		
	}




//
//	/**
//	 * extract the smallest subtree of tree1
//	 * taht contain all nodes of the template_nodes list
//	 * check every node
//	 * if it is not in the template_nodes list remove it
//	 * @param _template list of template nodes
//	 * @param _tree tree to extract from
//	 * @return tree that only contain nodes that are in template_nodes
//	 */
//	public TagNode getSmallestSubtree(ArrayList<Integer> _template, TagNode _tree){
//		
//		TagNode templateTree = _tree;
//		
//		// get all nodes of the tree1
//		ArrayList<TagNode> tn = new ArrayList<TagNode>();
//		getDescendants(templateTree, tn);
//		
//
//		
//		//counter for removed nodes
//		int o = 0;
//		
//		//check each node of tree1
//		for( TagNode n : tn){
//			
//			int u = getHashOfNode(n);
//			
//			if(!_template.contains(u)){
//				
//				// for getting content remove length in if and template has to contain u
//				if(n.getChildTags().length==0 && n.removeFromTree()){
//					o++;
//					//System.out.println(n.getName()+n.getAttributeByName("id"));
//				}
//				
//			}
//			
//		}
//		
//		
//		
//		System.out.println(o+" Knoten gelöscht vom templateTree");
//		
//		return templateTree;
//		
//	}
	
	
	/**
	 * post order traversal of a given node
	 * @param _node root node
	 * @param _tagList ordered list
	 */
	private static void getDescendants(Node _node, ArrayList<Node> _tagList){
		
		NodeList a = _node.getChildNodes();
		
		for( int u=0; u<a.getLength(); u++){
			
			getDescendants(a.item(u), _tagList);
			
			_tagList.add(a.item(u));
			
			
		}

		
	}
	
	
	
	/**
	 * calculate hash of node by its name, and id,class,href attributes
	 * @param _tn Node to hash
	 * @return hash of this node
	 */
	private int getHashOfNode(TagNode _tn){
		
		String f = _tn.getName()+_tn.getAttributeByName("href")+_tn.getAttributeByName("alt")+_tn.getAttributeByName("title")+_tn.getAttributeByName("src");

		
		if(_tn.getChildTags().length == 0){
			f += _tn.getText();
		}
		
		return f.hashCode();
		
		
//		return (_tn.getName()+_tn.getText()).hashCode();
		
	}

	

	
	public boolean isHasChanged() {
		return hasChanged;
	}
	


}
