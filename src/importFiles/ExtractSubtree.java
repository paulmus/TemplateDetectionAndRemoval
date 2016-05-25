package importFiles;

import java.util.ArrayList;

import org.htmlcleaner.TagNode;

import algorithm.Backtracking;
import algorithm.RetrieveTemplate;
import algorithm.TreeMapping;

public class ExtractSubtree {
	
	/**
	 * first given tree/document
	 */
	TagNode tree1;
	
	/**
	 * second given tree/document
	 */
	TagNode tree2;
	
	/**
	 * reduces subtree of tree 1
	 */
	TagNode resultTree;
	


	
	/**
	 * hash equivalent of nodes that belong to template
	 */
	ArrayList<Integer> template_nodes = null;
	
	ArrayList<TagNode> template_nodes2 = null;
	
	/**
	 * backtracking matrix, processed by RTDM-TD
	 */
	Backtracking[][] backtracking = null;


	
	
	public ExtractSubtree(TagNode _tree1, TagNode _tree2){
		
		//initialise
		tree1 = _tree1;
		tree2 = _tree2;
		
		System.out.println("§§"+tree1.getAllElements(true).length);
		
		//process treeMapping
		TreeMapping tm = new TreeMapping();
		tm.RTDM_TD(tree1, tree2);
		backtracking = tm.getBacktracking();
		
		
		//extract the template out of tree1 by template_nodes
		RetrieveTemplate rt = new RetrieveTemplate(backtracking);
		rt.getTemplate();
		template_nodes = rt.getRes();
		template_nodes2 = rt.getRes2();
//		resultTree = getSmallestSubtree(template_nodes, tree1);
		
		resultTree = getSmallestSubtree2(template_nodes2, tree1);
		
		
		System.out.println("***"+template_nodes.size());
		System.out.println("***"+template_nodes2.size());
		
		
		
		
		
	}
	


	private TagNode getSmallestSubtree2(ArrayList<TagNode> _template_nodes, TagNode _tree) {
		TagNode tree = _tree;
		
		// get all nodes of the tree1
		ArrayList<TagNode> tn = new ArrayList<TagNode>();
		getDescendants(tree, tn);
		
		System.out.println("++++"+tn.size());
		
		
		

		
		//counter for removed nodes
		int o = 0;
		int z = 0;
		
		//check each node of tree1
		for( TagNode n : tn){
			
			z++;
			if(!containNode(_template_nodes,n)){
				
				// for getting content remove length in if and template has to contain u
				if(n.removeFromTree()){
					o++;
					//System.out.println(n.getName()+n.getAttributeByName("id"));
				}
				
			}
			
		}
		

		
		
		
		System.out.println(o+" Knoten gelöscht vom templateTree "+z);
		
		return tree;
	}

	
	private boolean containNode(ArrayList<TagNode> _list, TagNode _node){
		
		for(TagNode n : _list){
			if(comp(n,_node)){
				return true;
			}
		
		return false;
		
	}


	private boolean comp(TagNode n, TagNode _node) {
		
		String a = n.getName()+n.getText();
		String b = _node.getName()+n.getText();
		
		if(a.hashCode()==b.hashCode()){
			
			return true;
		}
		
		return false;
	}



	/**
	 * extract the smallest subtree of tree1
	 * taht contain all nodes of the template_nodes list
	 * check every node
	 * if it is not in the template_nodes list remove it
	 * @param _template list of template nodes
	 * @param _tree tree to extract from
	 * @return tree that only contain nodes that are in template_nodes
	 */
	public TagNode getSmallestSubtree(ArrayList<Integer> _template, TagNode _tree){
		
		TagNode templateTree = _tree;
		
		// get all nodes of the tree1
		ArrayList<TagNode> tn = new ArrayList<TagNode>();
		getDescendants(templateTree, tn);
		

		
		//counter for removed nodes
		int o = 0;
		
		//check each node of tree1
		for( TagNode n : tn){
			
			int u = getHashOfNode(n);
			
			if(!_template.contains(u)){
				
				// for getting content remove length in if and template has to contain u
				if(n.getChildTags().length==0 && n.removeFromTree()){
					o++;
					//System.out.println(n.getName()+n.getAttributeByName("id"));
				}
				
			}
			
		}
		
		
		
		System.out.println(o+" Knoten gelöscht vom templateTree");
		
		return templateTree;
		
	}
	
	
	/**
	 * post order traversal of a given node
	 * @param _node root node
	 * @param _tagList ordered list
	 */
	private static void getDescendants(TagNode _node, ArrayList<TagNode> _tagList){
		

		
		for( TagNode child : _node.getChildTagList()){
			
			getDescendants(child, _tagList);
			
			_tagList.add(child);
			
			
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
	
	public ArrayList<Integer> getTemplate_nodes() {
		return template_nodes;
	}
	
	public TagNode getResultTree() {
		return resultTree;
	}

}
