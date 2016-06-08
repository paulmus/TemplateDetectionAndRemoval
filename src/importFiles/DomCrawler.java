package importFiles;


import java.io.File;
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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import algorithm.ExtractSubtree;


public class DomCrawler {
	
	
	private final int MAX_DOCUMENTS_TEMPLATE = 10;
	
	private Document templateTree = null;
	
	private Document manualTemplateTree = null;
	
	ArrayList<String> filenames = null;
	
	
	/**
	 * name of the folder (website) working on
	 */
	private String folderName = null;
	
	/**
	 * all websites parsed
	 */
	ArrayList<Document> doms = null;
	
	ArrayList<Node> contentDoms = null;
	
	
	public ArrayList<Node> getContentDoms() {
		return contentDoms;
	}

	ArrayList<Node> templateDoms = null;
	

	int l = 0;
	
	public DomCrawler( String _folder ){
		
		
		folderName = _folder;
		
		//retrieve all documents in folder
		readDoms();
		

		

		
		
			
			
		
		


		
	}
	
	public void processFolder(){
		getTemplates();
		
		readTemplateDom();
		
		getContents();
	}




	private void readTemplateDom() {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	

		
		String templateFile = "html/"+folderName+"/template/template.html";

		
		File f = new File(templateFile);
		
		if(f.isFile() && f.exists()){

			try{		
				
				Document doc = builder.parse(f);

					
				if(doc != null){
					templateTree = doc;
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
					
					
		}else{
			System.err.println(templateFile+" nicht gefunden!");
		}
		
		
		
		templateFile = "manually/"+folderName+".html";

		
		f = new File(templateFile);
		
		if(f.isFile() && f.exists()){

			try{		
				
				Document doc = builder.parse(f);

					
				if(doc != null){
					manualTemplateTree = doc;
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
					
					
		}else{
			System.err.println("Manuelles Template existiert nicht!");
		}
		

		
	}


	

	private void getTemplates(){
		
		
		int counter = 0;
		
		int max = MAX_DOCUMENTS_TEMPLATE;
		
		if(max > doms.size()){
			max = doms.size();
		}
		
		File lk = new File("html/"+folderName+"/template/");
		lk.mkdirs();
		
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
			
			if(templateDoms != null){
				templateDoms.add(resultTree.cloneNode(true));
			}else{
				templateDoms = new ArrayList<Node>();
				templateDoms.add(resultTree.cloneNode(true));
			}
			
			if(es.isHasChanged()){ //if has changed output file
				try {
					
					File f = null;

					f = new File("html/"+folderName+"/template/template.html");

					//f = new File("html/"+folderName+"/template/"+(counter++)+".html");
					//output all templates

					
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
		
		File lk = new File("html/"+folderName+"/content/");
		lk.mkdirs();
		
		//process the documents
		for(int i = 0; i<doms.size(); i++){
			
			ExtractSubtree es = new ExtractSubtree(doms.get(i),templateTree);
			resultTree = es.getContentTree();
			
			if(contentDoms != null){
				contentDoms.add(resultTree.cloneNode(true));
			}else{
				contentDoms = new ArrayList<Node>();
				contentDoms.add(resultTree.cloneNode(true));
			}
			
			

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
	private void readDoms(){
		

		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		int q = 0;

		
		String newFolder = "html/"+folderName+"/cleaned";

		
		File f = new File(newFolder);
		
		if(f.isDirectory() && f.exists()){
			
			//get all files in the directory
			File[] docs = f.listFiles();
			
			
			
			
			
			
			
			for( File z : docs ){
				
				
				//remove hidden files
				if(!z.isHidden()){
					
					
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
	
	


	

	
	public ArrayList<Document> getDoms() {
		return doms;
	}
	
	public Document getTemplateTree() {
		return templateTree;
	}

	public ArrayList<String> getFilenames() {
		return filenames;
	}

	public String getFolderName() {
		return folderName;
	}
	
	public Document getManualTemplateTree() {
		return manualTemplateTree;
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
	

}
