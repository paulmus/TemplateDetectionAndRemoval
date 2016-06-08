package algorithm;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author paulmuschiol
 *
 */
public class RetrieveTemplate {
	
	/**
	 * num of childs of tree1
	 */
	int m = 0;
	
	
	/**
	 * num of childs of tree2
	 */
	int n = 0;

	
	/**
	 * backtracking matrix of processing the TreeMatching
	 */
	Backtracking[][] B = null;
	
	

	
	ArrayList<Node> res = null;
	
	public RetrieveTemplate(Backtracking[][] _backtracking){
		B = _backtracking;
		
		m = B.length;
		
		n = B[0].length;
		

		
		res = new ArrayList<Node>();
	}
	
	
	/**
	 * given algorithm of paper
	 * start in the lower right corner
	 * add to template_nodes if and only if
	 * it is an update or no_op operation
	 * 
	 * if it is no_op
	 * recursively get a next matrix from Backtracking matrix
	 * 
	 * similiar to StringDistance algs
	 */
	public void getTemplate(){
		
		
		int i = m;
		int j = n;
		
		
		
		while(i>1 && j>1){
			
//			System.out.println((i-1)+" "+(j-1)+"cost"+B[i-1][j-1].getCost()+" "+B[i-2][j-2].getCost()+"mode"+B[i-1][j-1].getOperation()+"node");
			

			
			if(B[i-1][j-1].getOperation() == 1){ //delete

				i--;
				
				//go left
			}else if(B[i-1][j-1].getOperation() == 2){ //insert

				j--;
				
				//go right
			}else if(B[i-1][j-1].getOperation() == 3 && B[i-2][j-2].getCost()==B[i-1][j-1].getCost()){  //update
				Node src = B[i-1][j-1].getSource();
	
				res.add(src);
				
				if(src.hasChildNodes()){
					ArrayList<Node> d = new ArrayList<Node>();
					getDescendants(src,d);
					
					res.addAll(d);
				}
				
				i--;
				j--;
				
				//go left and up
			}else if(B[i-1][j-1].getOperation() == 4){ //no_op
				
				
				
				Node src = B[i-1][j-1].getSource();
				res.add(src);
				
				//process recursive call
				RetrieveTemplate recursive = new RetrieveTemplate(B[i-1][j-1].getNext());
				recursive.getTemplate();
				res.addAll(recursive.getRes());
				
				
				
//				if(recursive.getRes2().isEmpty()){ // there is an duplicate node inside
//					//dont add it, because there is an left fitting node maybe
//					j--;
//				}else{
//					res2.add(src);
//					res2.addAll(recursive.getRes2());
//					i--;
//					j--;
//				}
				
				i--;
				j--;


				
				
				//go left and up
			}else{
				i--;
				j--;
			}
			
			
			
		}
		
		
		

		

		
	}
	



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
	
	
	public ArrayList<Node> getRes() {
		
		
		return res;
	}




	
	

}
