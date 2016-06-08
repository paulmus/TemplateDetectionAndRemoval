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
	
	
	/**
	 * list of hash equivalents of nodes that
	 * belong to the template
	 */
	ArrayList<Integer> res = null;
	
	ArrayList<Node> res2 = null;
	
	public RetrieveTemplate(Backtracking[][] _backtracking){
		B = _backtracking;
		
		m = B.length;
		
		n = B[0].length;
		
		
		
		res = new ArrayList<Integer>();
		
		res2 = new ArrayList<Node>();
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
			
			System.out.println((i-1)+" "+(j-1)+"cost"+B[i-1][j-1].getCost()+" "+B[i-2][j-2].getCost()+"mode"+B[i-1][j-1].getOperation()+"node"+B[i-1][j-1].getSource().getNodeName()+B[i-1][j-1].getSource().getTextContent().trim());
			

			
			if(B[i-1][j-1].getOperation() == 1){ //delete
				
				System.out.println("±±±±±±±±"+B[i-1][j-1].getSource().getNodeName());
				i--;
				
				//go left
			}else if(B[i-1][j-1].getOperation() == 2){ //insert

				System.out.println("±±±±±±±±"+"±±±±±±±±"+B[i-1][j-1].getSource().getNodeName());
				j--;
				
				//go right
			}else if(B[i-1][j-1].getOperation() == 3 && B[i-2][j-2].getCost()==B[i-1][j-1].getCost()){  //update
				Node src = B[i-1][j-1].getSource();

				
				res2.add(src);
				
				System.out.println("++"+src.getNodeName());
				
				if(src.hasChildNodes()){
					ArrayList<Node> d = new ArrayList<Node>();
					getDescendants(src,d);
					

					
					res2.addAll(d);
				}
				
				
				
				
				

				
				
				i--;
				j--;
				
				//go left and up
			}else if(B[i-1][j-1].getOperation() == 4){ //no_op
				
				
				
				Node src = B[i-1][j-1].getSource();
				res2.add(src);
				
				
				
				System.out.println("+++"+src.getNodeName());
				//process recursive call
				RetrieveTemplate recursive = new RetrieveTemplate(B[i-1][j-1].getNext());
				recursive.getTemplate();
				System.out.println("&&&&&"+recursive.getRes2().size());
				res2.addAll(recursive.getRes2());
				
				
				
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
	
	public ArrayList<Node> getRes2() {
		// TODO Auto-generated method stub
		return res2;
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


	public ArrayList<Integer> getRes() {
		return res;
	}



	
	

}
