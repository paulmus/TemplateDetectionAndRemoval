package importFiles;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;

/**
 * @author paulmuschiol
 * produce valid xml files of extracted files
 */
public class cleanHTML {
	
	
	/**
	 * html cleaner config
	 * http://htmlcleaner.sourceforge.net/parameters.php
	 */
	CleanerProperties props = new CleanerProperties();
	
	/**
	 * serializer to output documents
	 */
	final SimpleXmlSerializer htmlSerializer = new SimpleXmlSerializer(props);
	
	
	
	public cleanHTML(){
		

	}
	
	/**
	 * read in file 
	 * correct it 
	 * output valid xml
	 * @param _file file to parse
	 */
	public void cleanFile(String _file){
		
		
		
		//parser configuration
		props.setOmitComments(true);
		props.setTransResCharsToNCR(true);
		props.setTranslateSpecialEntities(true);
		props.setNamespacesAware(false);
		props.setAdvancedXmlEscape(false);
		
		

		
		try{
			//read in
			File inputFile = new File(_file.substring(0, _file.length()-1));
			FileInputStream fis = new FileInputStream( inputFile );
			
			TagNode tagNode = new HtmlCleaner(props).clean(	fis	);
			
			String[] u = _file.split("/");
			String newFilename = "";
			String newPathname = "";
			for(int i=0; i<u.length; i++){
				if(u[i].isEmpty()){//first element -> absolute path
					newPathname += "/";
				}else if(i == 0){// first element -> realtive path
					newPathname += u[i];
				}else if(i == u.length-1){//last element
					newFilename += "/"+u[i];
				}else{
					newPathname += "/"+u[i];
				}
			}
			
			//output cleaned file
			newPathname += "/cleaned";
			File newPath = new File(newPathname);
			if(!newPath.exists()){
				newPath.mkdirs();
			}
			
			String finalFilename = newPathname+newFilename;
			
			
			
			
			fis.close();
			
			htmlSerializer.writeToFile(tagNode, finalFilename, "utf-8");
			

			

		
		}catch(IOException e){
			e.printStackTrace();
		}

		
	}
	
	/**
	 * check folder for subfolders and clean all html files inside
	 * @param _path path to check
	 */
	public void cleanAllFiles(String _path){
		
		File f = new File(_path);
		
		if(f.isDirectory() && f.exists()){
			String[] files = f.list();
			
			for(int i=0; i<files.length; i++){
				
				File o = new File(_path+files[i]);
				if(o.isDirectory() && !files[i].contains("cleaned") && !_path.contains("cleaned")){
					
					cleanAllFiles(_path+files[i]+"/");
				}else if(o.isFile() && !o.isHidden() && files[i].contains(".html")){
					
					cleanFile(_path+files[i]+"/");
					
				}
				
			}
		}
		
	}
	
	
	public static void main (String[] args){
		new cleanHTML().cleanAllFiles("html/");
	}

}
