package algorithm;

import java.io.IOException;
import java.util.ArrayList;

import org.htmlcleaner.TagNode;




public class TreeMapping {
	
	TagNode tree1 = null;
	TagNode tree2 = null;
	
	ArrayList<ArrayList<TagNode>> descendants1 = null;
	ArrayList<ArrayList<TagNode>> descendants2 = null;
	
	Backtracking[][] backtracking;
	

	
	public int RTDM_TD(TagNode _node1, TagNode _node2){
		
		int[][] Cost;
		
		
		tree1 = _node1;
		tree2 = _node2;
		
		int m = tree1.getChildTagList().size();
		int n = tree2.getChildTagList().size();
		
		Cost = new int[m+1][n+1];
		Cost[0][0] = 0;
		backtracking = new Backtracking[m+1][n+1];
		backtracking[0][0] = new Backtracking();
		backtracking[0][0].setCost(0);
				

		
		
		
		
		
		
		
		descendants1 = new ArrayList<ArrayList<TagNode>>();
		descendants2 = new ArrayList<ArrayList<TagNode>>();
		
		
		int temp = 1;
		for( TagNode i : tree1.getChildTagList()){
			ArrayList<TagNode> t = new ArrayList<TagNode>();
			getDescendants(i, t);
			descendants1.add(t);
			
			int c = Cost[temp-1][0] + t.size();
			Cost[temp][0] = c;
			backtracking[temp][0] = new Backtracking();
			backtracking[temp][0].setCost(c);
			
			temp++;
			
			
			
//			for(TagNode e : t){
//				System.out.println(e.getName());
//				System.out.println(e.getText());
//			}
//			System.out.println("###"+t.size());
		}
		
		temp=1;
		for( TagNode i : tree2.getChildTagList()){
			ArrayList<TagNode> t = new ArrayList<TagNode>();
			getDescendants(i, t);
			descendants2.add(t);
			
			int c = Cost[0][temp-1] + t.size();
			Cost[0][temp] = c;
			backtracking[0][temp] = new Backtracking();
			backtracking[0][temp].setCost(c);
			
			
			temp++;
			
//			for(TagNode e : t){
//				System.out.println(e.getName());
//			}
//			System.out.println("###"+t.size());
		}
		
		
		
		for(int i = 1; i<=m ; i++){
			for(int j = 1; j<=n ; j++){
				
				TreeMapping alg = null;
				Backtracking[][] B = null;
				int Ci = descendants1.get(i-1).size();
				int Cj = descendants2.get(j-1).size();
				int D = Cost[i-1][j] + Ci + 1;
				int I = Cost[i][j-1] + Cj + 1;
				int S = Cost[i-1][j-1];
				int N = -1;
				
				if(identicalSubtree(i,j)){
					
				}else if(identicalNode(tree1.getChildTags()[i-1],tree2.getChildTags()[j-1])){
					
					
					
//					System.out.println(i+"recursion"+j);
					alg = new TreeMapping();
					N = S + alg.RTDM_TD(tree1.getChildTags()[i-1],tree2.getChildTags()[j-1]);


					
					
					
				}else{
					

					S = 1;
					if(descendants1.get(i-1).isEmpty()){
						S = S + descendants2.get(j-1).size();
					}else if(descendants2.get(j-1).isEmpty()){
						S = S + descendants1.get(i-1).size();
					}
					
				}
				
				
				
				if(N == -1){
					Cost[i][j] = Math.min(D,I);
					Cost[i][j] = Math.min(Cost[i][j],S);
				}else{
					Cost[i][j] = Math.min(D,I);
					Cost[i][j] = Math.min(Cost[i][j], S);
					Cost[i][j] = Math.min(Cost[i][j],N);
				}
				
				backtracking[i][j] = new Backtracking();
				backtracking[i][j].setCost(Cost[i][j]);
				backtracking[i][j].setSource(tree1.getChildTags()[i-1]);
				if(alg != null){
					backtracking[i][j].setNext(alg.getBacktracking());
				}
				
				if(Cost[i][j] == D){
					backtracking[i][j].setOperation(1);
				}else if(Cost[i][j] == I){
					backtracking[i][j].setOperation(2);
				}else if(Cost[i][j] == N){
					backtracking[i][j].setOperation(4);
				}else if(Cost[i][j] == S){
					backtracking[i][j].setOperation(3);
				}
				
				
				
				
				
				
				
				
				
				
			}
			
		}
		
		
		
		
		
		
		
		
		
//		
//		for(int i = 0; i<=m ; i++){
//			for(int j = 0; j<=n ; j++){
//				System.out.print(Cost[i][j]+" ");
//			}
//			System.out.println("");
//		}
//		System.out.println("");
//		
//		
//		for(int i = 1; i<=m ; i++){
//			String cost = "";
//			String src ="";
//			String ma = "";
//			for(int j = 1; j<=n ; j++){
//				
//				cost += backtracking[i][j].getCost()+"    ";
//				src += backtracking[i][j].getSource().getName()+"  ";
//				ma += backtracking[i][j].getOperation()+"    ";
//				
//					
//			}
//			System.out.println(cost);
//			System.out.println(src);
//			System.out.println(ma);
//			System.out.println("");
//		}
//		System.out.println("");
		

		
		
		

		


		
		return Cost[m][n];
		
		
		
		
		
		
		
		
	}
	
	public Backtracking[][] getBacktracking() {
		return backtracking;
	}

	private boolean identicalSubtree(int _index1, int _index2) {
		
		if(identicalNode(tree1.getChildTags()[_index1-1],tree2.getChildTags()[_index2-1])){
			
			indenticalNodesWithContent(_index1-1,_index2-1);
			
		}else{
			return false;
		}
		
		return false;
		
	}

	private boolean indenticalNodesWithContent(int _index1, int _index2) {
		
		ArrayList<TagNode> nodes1 = descendants1.get(_index1);
		ArrayList<TagNode> nodes2 = descendants2.get(_index2);
		
		int q = nodes1.size();
		
		//wrong size -> not equal
		if(q != nodes2.size()){
			return false;
		}
		
		for(int i=0; i<q; i++){
			
			if(!compareNode(nodes1.get(i),nodes2.get(i))){
				return false;
			}
		}
		
		return true;
		
		
	}

	private boolean compareNode(TagNode _tagNode1, TagNode _tagNode2) {
		int t1 = (_tagNode1.getName()+_tagNode1.getText()).hashCode();
		int t2 = (_tagNode2.getName()+_tagNode2.getText()).hashCode();
		
//		System.out.println(_tagNode1.getName()+_tagNode1.getText()+"##"+t1);
//		System.out.println(_tagNode2.getName()+_tagNode2.getText()+"##"+t2);
		
		if(t1==t2){
			return true;
		}
		
		return false;
	}

	private boolean identicalNode(TagNode _tagNode1, TagNode _tagNode2) {
		
//		System.out.println(_tagNode1.getName()+_tagNode2.getName());
		
		if(_tagNode1.getName().equals(_tagNode2.getName())){
			return true;
		}else{
			return false;
		}
		
	}

	private static void getDescendants(TagNode _node, ArrayList<TagNode> _tagList){
		
		for( TagNode child : _node.getChildTagList()){
			
			getDescendants(child, _tagList);
			
			_tagList.add(child);
			
			
		}

		
	}
	
	
	public static void main(String[] args) throws IOException{

	}

}
