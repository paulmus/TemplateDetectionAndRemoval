package algorithm;

import java.util.ArrayList;
import java.util.Collection;

import org.htmlcleaner.TagNode;

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
	
	ArrayList<TagNode> res2 = null;
	
	public RetrieveTemplate(Backtracking[][] _backtracking){
		B = _backtracking;
		
		m = B.length;
		
		n = B[0].length;
		
		
		
		res = new ArrayList<Integer>();
		
		res2 = new ArrayList<TagNode>();
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
			
//			System.out.println(i+" "+j);
			
			if(B[i-1][j-1].getOperation() == 1){ //delete
				
				System.out.println("ins");
				j--;
				
				//go left
			}else if(B[i-1][j-1].getOperation() == 2){ //insert
				
				System.out.println("ins");
				i--;
				
				//go right
			}else if(B[i-1][j-1].getOperation() == 3 && B[i-2][j-2].getCost()==B[i-1][j-1].getCost()){  //update
				TagNode src = B[i-1][j-1].getSource();
				res.add(getHashOfNode(src));
				
				res2.add(src);
				
//				System.out.println("#"+src.getName()+src.getAttributeByName("id")+src.getAttributeByName("class")+src.getText()+"\n");
				
				
				//order is important, first all subnodes then the real one
//				ArrayList<Integer> descs = new ArrayList<Integer>();
//				getDescendants(src,descs); //-> not neccessary to add descendants, because deleting of whole subtree in this case
				
				ArrayList<TagNode> d = new ArrayList<TagNode>();
				getDescendants(src,d);
				//System.out.println("**"+d.size());
				
				res2.addAll(d);
				
//				
//				res.addAll(descs);
				
				

				
				
				i--;
				j--;
				
				//go left and up
			}else if(B[i-1][j-1].getOperation() == 4){ //no_op
				
				
				TagNode src = B[i-1][j-1].getSource();
				res.add(getHashOfNode(src)); 
				
				res2.add(src);
				//add the root node -> it is template,
				//decide for each descenadnt itself if its template or not
//				System.out.println("##"+src.getName()+src.getText()+"\n");
				
				//process recursive call
				RetrieveTemplate recursive = new RetrieveTemplate(B[i-1][j-1].getNext());
				recursive.getTemplate();
				
				//System.out.println("###"+recursive.getRes().size());
				//System.out.println("*"+recursive.getRes2().size());
				res2.addAll(recursive.getRes2());
				
				res.addAll(recursive.getRes());
				
				i--;
				j--;
				
				//go left and up
			}
			
		}
		
		
		
		

		
	}
	
	public ArrayList<TagNode> getRes2() {
		// TODO Auto-generated method stub
		return res2;
	}


	/**
	 * post order traversal of a given node
	 * @param _node root node
	 * @param _tagList ordered list
	 */
	private void getDescendants(TagNode _node, ArrayList<TagNode> _tagList){
		

		
		for( TagNode child : _node.getChildTagList()){
			
			getDescendants(child, _tagList);
			
			_tagList.add(child);
			
			
		}

		
	}


	public ArrayList<Integer> getRes() {
		return res;
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


//	private void addDescendants(TagNode src, ArrayList<Integer> res2) {
//		
//		ArrayList<TagNode> descs = new ArrayList<TagNode>();
//		
//		getDescendants(src,descs);
//		
//		for(TagNode tn : descs){
//			res2.add(getHashOfNode(tn));
//		}
//		
//		
//	}
	
	

}
