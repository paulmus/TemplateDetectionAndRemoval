package importFiles;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;


public class DomCrawler {
	
	/**
	 * name of the folder (website) working on
	 */
	private String folderName = null;
	
	/**
	 * all websites parsed
	 */
	ArrayList<TagNode> doms = null;
	
	/**
	 * html cleaner config
	 * http://htmlcleaner.sourceforge.net/parameters.php
	 */
	CleanerProperties props = new CleanerProperties();
	
	/**
	 * serializer to output documents
	 */
	final SimpleHtmlSerializer htmlSerializer = new SimpleHtmlSerializer(props);
	
	int l = 0;
	
	public DomCrawler( String _folder ){
		
		//ignore comments
		props.setOmitComments(true);
		//remove script and style
		props.setPruneTags("script,style");
		
		
		folderName = _folder;
		
		//retrieve all documents in folder
		getDoms();
		

		
		TagNode resultTree = null;
		
		//process the documents
		for(int i = 0; i<5; i++){
			
			System.out.println("#"+i);
			
			ExtractSubtree es = null;
			
			if(resultTree == null){
				
				es = new ExtractSubtree(doms.get(i),doms.get(i+1));
				i++;
				
				resultTree = es.getResultTree();
				
				try {
					
					File k = new File("html/"+folderName+"/template");
					if(k.exists() && k.isDirectory()){
						htmlSerializer.writeToFile(resultTree, "html/"+folderName+"/template/template"+(l++)+".html", "utf-8");
					}else{
						k.mkdir();
						htmlSerializer.writeToFile(resultTree, "html/"+folderName+"/template"+(l++)+".html", "utf-8");
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				
				if(matchTemplate(doms.get(i),resultTree)){
					
					//matches -> nothing to do
					
					System.out.println("match");
					
				}else{
					es = new ExtractSubtree(resultTree,doms.get(i));
					
					resultTree = es.getResultTree();
					
					try {
						
						File k = new File("html/"+folderName+"/template");
						if(k.exists() && k.isDirectory()){
							htmlSerializer.writeToFile(resultTree, "html/"+folderName+"/template/template"+(l++)+".html", "utf-8");
						}else{
							k.mkdir();
							htmlSerializer.writeToFile(resultTree, "html/"+folderName+"/template"+(l++)+".html", "utf-8");
						}
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
				
			}
			
			
			
			
		
		}


		
	}
	
	
	/**
	 * retrieve all doms from the specified folder 
	 * add thme to the list of doms
	 */
	private void getDoms(){
		

		int q = 0;

		
		String newFolder = null;
		if(folderName.endsWith("/")){
			newFolder = "html/"+folderName+"";
		}else{
			newFolder = "html/"+folderName+"/";
		}
		
		File f = new File(newFolder);
		
		if(f.isDirectory() && f.exists()){
			
			//get all files in the directory
			File[] docs = f.listFiles();
			
			
			
			for( File z : docs ){
				
				
				//remove hidden files
				if(!z.isHidden()){
					
					System.out.println(z.getName()+q++);
					
					try{		

					
					TagNode tagNode = new HtmlCleaner(props).clean(
							new FileInputStream(z)
						);
					
					//tagNode = tagNode.findElementByName("body", false);
					
					if(tagNode != null){
						if(doms != null){
							
							doms.add(tagNode);
						}else{
							doms = new ArrayList<TagNode>();
							doms.add(tagNode);
						}
					}
					
					}catch(Exception e){
						
					}
					
					
				}
				
				
				
				
				
				
			}
			
		}
		

		
	}
	
	
	/**
	 * check if the all nodes of the templateTree are also in
	 * the newTree
	 * two nodes can be not in the tree
	 * for menu items with class = "actualPage" ore something
	 * @param _newTree tree to compare with
	 * @param _templateTree returned templateTree of the last step
	 * @return if the _newTree match all template nodes
	 */
	private boolean matchTemplate(TagNode _newTree, TagNode _templateTree){
		
		//get all nodes of the template Tree as hash
		ArrayList<TagNode> template_nodes = new ArrayList<TagNode>();
		getDescendants(_templateTree, template_nodes);
		ArrayList<Integer> template_nodeHash = new ArrayList<Integer>();
		System.out.println("template_nodesize"+template_nodes.size());
		for(TagNode tn : template_nodes){
			template_nodeHash.add(getHashOfNode(tn));
		}
		
		
		//get all nodes of the new Tree as hash
		ArrayList<TagNode> nodes = new ArrayList<TagNode>();
		getDescendants(_newTree, nodes);
		ArrayList<Integer> nodeHash = new ArrayList<Integer>();
		System.out.println("nodesize"+nodes.size());
		for(TagNode tn : nodes){
			nodeHash.add(getHashOfNode(tn));
		}
		
		
		//check if every template node is also in the new tree
		
		int errorcounter = 0;
		
		int q = 0;
		
		for(int template : template_nodeHash){
			
			if(!nodeHash.contains(template)){
				
				TagNode u = template_nodes.get(q);
				//System.out.println(u.getName()+" "+u.getAttributeByName("id")+" "+u.getAttributeByName("class"));
				errorcounter++;
			}else{
				q++;
			}
			
			
		}
		
		System.out.println("right nodes"+q+" wrong nodes "+errorcounter);
		
		if(errorcounter<2){
			return true;
		}else{
			return false;
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
	
	public static void main(String[] args){
		new DomCrawler("wilhelm-meier-online.de");
	}

}
