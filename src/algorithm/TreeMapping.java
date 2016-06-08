package algorithm;

import java.util.ArrayList;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class TreeMapping {
	
	Node tree1 = null;
	Node tree2 = null;
	
	ArrayList<ArrayList<Node>> descendants1 = null;
	ArrayList<ArrayList<Node>> descendants2 = null;
	
	Backtracking[][] backtracking;
	
	

	
	public int RTDM_TD(Node _node1,Node _node2){
		
		int[][] Cost;
		
		
		tree1 = _node1;
		tree2 = _node2;
		
		removeEmptyNodes(tree1);
		removeEmptyNodes(tree2);
		
		
		
		int m = tree1.getChildNodes().getLength();
		int n = tree2.getChildNodes().getLength();
		
		Cost = new int[m+1][n+1];
		Cost[0][0] = 0;
		backtracking = new Backtracking[m+1][n+1];
		backtracking[0][0] = new Backtracking();
		backtracking[0][0].setCost(0);
		
		
		NodeList tree1Childs = tree1.getChildNodes();
		NodeList tree2Childs = tree2.getChildNodes();
				

		
		
		
		
		
		
		
		descendants1 = new ArrayList<ArrayList<Node>>();
		descendants2 = new ArrayList<ArrayList<Node>>();
		
		
		int temp = 1;
		
		for( int i=0; i<tree1Childs.getLength(); i++){
			//post order traversal for child node i
			ArrayList<Node> t = new ArrayList<Node>();
			getDescendants(tree1Childs.item(i), t);
			descendants1.add(t);

			
			int c = Cost[temp-1][0] + t.size();
			
			Cost[temp][0] = c;
			backtracking[temp][0] = new Backtracking();
			backtracking[temp][0].setCost(c);
			
			temp++;
			

		}
		
		temp=1;
		
		for( int i = 0; i<tree2Childs.getLength(); i++){
			ArrayList<Node> t = new ArrayList<Node>();
			getDescendants(tree2Childs.item(i), t);
			descendants2.add(t);
			
			int c = Cost[0][temp-1] + t.size();
			Cost[0][temp] = c;
			backtracking[0][temp] = new Backtracking();
			backtracking[0][temp].setCost(c);
			
			
			temp++;
			
		}
		
		
		
		for(int i = 1; i<=m ; i++){
			for(int j = 1; j<=n ; j++){
				
				

				
				TreeMapping alg = null;
				Backtracking[][] B = null;
				int Ci = descendants1.get(i-1).size();//check costs
				int Cj = descendants2.get(j-1).size();
				int D = Cost[i-1][j] + Ci +1;
				int I = Cost[i][j-1] + Cj +1;
				int S = Cost[i-1][j-1];
				int N = -1;
				
				if(identicalSubtree(tree1Childs.item(i-1),tree2Childs.item(j-1))){
					
					
//					System.out.println("matchSubtree");
					

				}else if(identicalNode(tree1Childs.item(i-1),tree2Childs.item(j-1))){
					


					
//					System.out.println("macthNode");
					alg = new TreeMapping();
					N = S + alg.RTDM_TD(tree1Childs.item(i-1),tree2Childs.item(j-1));

					
					
					
					
				}else{

					
//					System.out.println("nomatch");

					S = S+1;
					if(descendants1.get(i-1).isEmpty()){
						S = S + descendants2.get(j-1).size();
						if(tree1Childs.item(i-1).getNodeType() == Node.TEXT_NODE){ //special case of an single text node as leaf
							S = -1;
						}
					}else if(descendants2.get(j-1).isEmpty()){
						S = S + descendants1.get(i-1).size();
					}else{
						// nothing is an leaf -> delete or insert this node
						
						S = -1;
					}
					
				}
				
				if(S == -1 && N == -1){
					
					Cost[i][j] = Math.min(D,I);
					
				}else if(N == -1){
					
					Cost[i][j] = Math.min(D,I);
					Cost[i][j] = Math.min(Cost[i][j],S);
					
				}else{
					
					Cost[i][j] = Math.min(D,I);
					Cost[i][j] = Math.min(Cost[i][j],N);
					
				}
				
				backtracking[i][j] = new Backtracking();
				backtracking[i][j].setCost(Cost[i][j]);
				backtracking[i][j].setSource(tree1Childs.item(i-1));
				if(alg != null){
					backtracking[i][j].setNext(alg.getBacktracking());
				}
				
				
				if(Cost[i][j] == D){
					
					//special case if it is on a border of the matrix
					if(D == I && i == 1 && j > 0){ //Delete Cost is equal to Insert Cost; top border of the matrix and there is space to the left
						backtracking[i][j].setOperation(2);//Set insert instead
					}else{
						backtracking[i][j].setOperation(1);
					}
					
				}else if(Cost[i][j] == I){
					
					//special case if it is on a border of the matrix
					if(D == I && j == 1 && i > 0){ //Delete Cost is equal to Insert Cost; top border of the matrix and there is space to the left
						backtracking[i][j].setOperation(1);//Set delete instead
					}else{
						backtracking[i][j].setOperation(2);
					}
					
				}else if(Cost[i][j] == N){
					backtracking[i][j].setOperation(4);
				}else if(Cost[i][j] == S){
					backtracking[i][j].setOperation(3);
				}
				
				
				
				
				
				
				
				
				
				
			}
			
		}
		
		
		
		

		
		
		
//		for(int i = 0; i<=m ; i++){
//			for(int j = 0; j<=n ; j++){
//				System.out.print(Cost[i][j]+" ");
//			}
//			System.out.println("");
//		}
//		System.out.println("");
		
		
//		for(int i = 1; i<=m ; i++){
//			String cost = "";
//			String src ="";
//			String ma = "";
//			for(int j = 1; j<=n ; j++){
//				
//				cost += backtracking[i][j].getCost()+"    ";
//				src += backtracking[i][j].getSource().getNodeName()+"  ";
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
	
	
	private static Node removeEmptyNodes(Node _n){
		NodeList children = _n.getChildNodes();
		
		for(int u = 0; u<children.getLength();u++){
			Node child = children.item(u);
			String l = child.getTextContent().trim();
			if(child.getNodeType() == Node.TEXT_NODE && l.isEmpty()){

				_n.removeChild(child);
			}else{
				removeEmptyNodes(child);
			}
		}
		
		return _n;
		
	}
	
	
	public Backtracking[][] getBacktracking() {
		return backtracking;
	}
	
//	The two nodes are of the same type.
//	The following string attributes are equal: nodeName, localName, namespaceURI, prefix, nodeValue . This is: they are both null, or they have the same length and are character for character identical.
//	The attributes NamedNodeMaps are equal. This is: they are both null, or they have the same length and for each node that exists in one map there is a node that exists in the other map and is equal, although not necessarily at the same index.
//	The childNodes NodeLists are equal. This is: they are both null, or they have the same length and contain equal nodes at the same index. Note that normalization can affect equality; to avoid this, nodes should be normalized before being compared.

	private boolean identicalSubtree(Node node1, Node node2) {
		
		//primitive Node
		if(node1.getNodeType() == Node.TEXT_NODE && node2.getNodeType() == Node.TEXT_NODE){
			if(node1.getTextContent().equals(node2.getTextContent())){
				return true;
			}else{
				return false;
				
			}
		}
		
		
		if(node1.isEqualNode(node2)){
			
			NodeList o = node1.getChildNodes();
			NodeList p = node2.getChildNodes();
			
			for( int i = 0; i<o.getLength(); i++){
				if(!identicalSubtree(o.item(i),p.item(i))){
					
					return false;
				}
			}
			
			return true;
			
		}else{

			return false;
		}
		

		
	}




	private boolean identicalNode(Node _tagNode1, Node _tagNode2) {
		


		if(_tagNode1.getNodeType() != Node.TEXT_NODE && _tagNode2.getNodeType() != Node.TEXT_NODE){
			
			String a = _tagNode1.getNodeName();
//			NamedNodeMap NNM1 = _tagNode1.getAttributes();
//			for(int q = 0; q<NNM1.getLength(); q++){
//				a += NNM1.item(q).getNodeName()+NNM1.item(q).getTextContent();
//				System.out.println(NNM1.item(q).getNodeName()+NNM1.item(q).getTextContent());
//			}
//			
			
			String b = _tagNode2.getNodeName();
//			NamedNodeMap NNM2 = _tagNode2.getAttributes();
//			for(int q = 0; q<NNM2.getLength(); q++){
//				b += NNM2.item(q).getNodeName()+NNM2.item(q).getTextContent();
//				System.out.println(NNM2.item(q).getNodeName()+NNM2.item(q).getTextContent());
//			}
			
			
			if(a.equals(b)){
				return true;
			}else{
				return false;
			}
		}else{
			String a = _tagNode1.getTextContent();
			String b = _tagNode2.getTextContent();
			
			if(a.equals(b)){
				return true;
			}else{
				return false;
			}
		}
		
	}

	
	private static void getDescendants(Node _node, ArrayList<Node> _tagList){
		
		NodeList a = _node.getChildNodes();
		
		for( int u=0; u<a.getLength(); u++){
			
			getDescendants(a.item(u), _tagList);
			
			_tagList.add(a.item(u));
			
			
		}

		
	}


}
