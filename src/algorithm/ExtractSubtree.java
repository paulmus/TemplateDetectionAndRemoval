package algorithm;

import java.util.ArrayList;

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
		template_nodes = rt.getRes();

		
	

		

		
		
		
		
		
		
		
	}
	
	public Node getSmallestSubtree(){
		SmallestSubtree(template_nodes, tree1);
		
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
				
				SmallestSubtree(_template_nodes,nl.item(p));
				
			}else{
				
				hasChanged = true;
				
				if(nl.item(p).getNodeName().equals("base")){
					continue;
				}
				
				//is no template node, dont go deeper
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
						if(!nl.item(p).getNodeName().equals("base")){
							
							nl.item(p).getParentNode().removeChild(nl.item(p));
							p--;
						}
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

	

	
	public boolean isHasChanged() {
		return hasChanged;
	}
	


}
