package importFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class ExtractContent {
	
	private TagNode treeToExtract = null;
	
	private TagNode templateTree = null;
	
	private String filename = null;
	
	private String foldername = null;
	
	
	/**
	 * html cleaner config
	 * http://htmlcleaner.sourceforge.net/parameters.php
	 */
	CleanerProperties props = new CleanerProperties();
	
	public ExtractContent(String _filename, String _foldername){
		
		//ignore comments
		props.setOmitComments(true);
		//remove script and style
		props.setPruneTags("script,style");

		filename = _filename;
		
		foldername = _foldername;
		
		getTree();
		
		ArrayList<TagNode> template = new ArrayList<TagNode>();
		getDescendants(templateTree,template);
		
		ArrayList<TagNode> content = new ArrayList<TagNode>();
		getDescendants(treeToExtract,content);
		
		ArrayList<Integer> templateHash = new ArrayList<Integer>();
		for(TagNode e : template){
			templateHash.add(getHashOfNode(e));
		}
		
		for(TagNode p : content){
			if(!templateHash.contains(getHashOfNode(p))){
				if(p.getChildTags().length == 0){
//					System.out.println("#"+p.getName());
				}else{
//					System.out.println("##"+p.getName()+p.getAttributeByName("id")+p.getAttributeByName("class"));
				}
				System.out.println(p.getText());
			}
		}
		
	}
	
	/**
	 * retrieve all doms from the specified folder 
	 * add thme to the list of doms
	 */
	private void getTree(){
		

		
		
		File f = new File("html/"+foldername+"/"+filename);
		
		if(f.isFile() && f.exists()){
			
			try{
				treeToExtract = new HtmlCleaner(props).clean(new FileInputStream(f));
			}catch(IOException e){
				e.printStackTrace();
			}

					
					
		}
		
		
		File g = new File("html/"+foldername+"/template/template0.html");
		
		if(g.isFile() && g.exists()){
			
			try{
				templateTree = new HtmlCleaner(props).clean(new FileInputStream(g));
			}catch(IOException e){
				e.printStackTrace();
			}

					
					
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
		new ExtractContent("189.html", "wilhelm-meier-online.de");
	}
	
	
	

}
