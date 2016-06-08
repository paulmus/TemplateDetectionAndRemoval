package importFiles;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import algorithm.ExtractSubtree;
import algorithm.TreeMapping;


public class DomCrawler {
	
	
	private final int MAX_DOCUMENTS_TEMPLATE = 15;
	
	private Document templateTree = null;
	
	ArrayList<String> filenames = null;
	
	
	/**
	 * name of the folder (website) working on
	 */
	private String folderName = null;
	
	/**
	 * all websites parsed
	 */
	ArrayList<Document> doms = null;
	
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
		
		getTemplates();
		
		getTemplateDom();
		
		getContents();
		

		
		
			
			
		
		


		
	}


	private void getTemplateDom() {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	

		
		String templateFile = null;
		if(folderName.endsWith("/")){
			templateFile = "html/"+folderName+"template/template.html";
		}else{
			templateFile = "html/"+folderName+"/template/template.html";
		}
		
		File f = new File(templateFile);
		
		if(f.isFile() && f.exists()){

			try{		
				
				Document doc = builder.parse(f);

					
				if(doc != null){
					templateTree = doc;
				}
			
			}catch(Exception e){
				
			}
					
					
		}
		

		
	}


	private void getTemplates(){
		
		
		int counter = 0;
		
		int max = MAX_DOCUMENTS_TEMPLATE;
		
		if(max > doms.size()){
			max = doms.size();
		}
		
		Node resultTree = null;
		
		//process the documents
		for(int i = 0; i<max; i++){
			
			System.out.println("#"+i);
			
			ExtractSubtree es = null;
			
			if(resultTree == null){
				
				es = new ExtractSubtree(doms.get(0),doms.get(1));
				i++;
				
				
				resultTree = es.getSmallestSubtree();
				
			}else{
				
	
				es = new ExtractSubtree(resultTree,doms.get(i));
				
				resultTree = es.getSmallestSubtree();
				
	
			
				
			}
			
			if(es.isHasChanged()){ //if has changed output file
				try {
					
					File f = null;
					if(i == MAX_DOCUMENTS_TEMPLATE-1){
						f = new File("html/"+folderName+"/template/template.html");
						
					}else{
						f = new File("html/"+folderName+"/template/"+(counter++)+".html");
						
					}
					f.createNewFile();
					FileOutputStream fos = new FileOutputStream(f);
					printDocument(resultTree,fos);
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void getContents(){
		
		
		Node resultTree = null;
		
		//process the documents
		for(int i = 0; i<doms.size(); i++){
			
			ExtractSubtree es = new ExtractSubtree(doms.get(i),templateTree);
			resultTree = es.getContentTree();
			

			try {
				
				File f = new File("html/"+folderName+"/content/"+filenames.get(i));
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				printDocument(resultTree,fos);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	/**
	 * retrieve all doms from the specified folder 
	 * add thme to the list of doms
	 */
	private void getDoms(){
		

		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		int q = 0;

		
		String newFolder = null;
		if(folderName.endsWith("/")){
			newFolder = "html/"+folderName+"cleaned";
		}else{
			newFolder = "html/"+folderName+"/cleaned";
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
						
						Document doc = builder.parse(z);


					
					if(doc != null){
						if(doms != null){
							filenames.add(z.getName());
							doms.add(doc);
						}else{
							doms = new ArrayList<Document>();
							filenames = new ArrayList<String>();
							doms.add(doc);
							filenames.add(z.getName());
						}
					}
					
					}catch(Exception e){
						
					}
					
					
				}
				
				
				
				
				
				
			}
			
		}
		

		
	}
	
	


	

	
	public static void printDocument(Node doc, OutputStream out) throws IOException, TransformerException {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(new DOMSource(doc), 
	         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
	
	public static void main(String[] args){
		new DomCrawler("scheufelen.com");
	}

}
