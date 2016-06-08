package importFiles;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.w3c.tidy.Tidy;

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
	
	public void cleanFile(String _file){
		
		
		
		//ignore comments
		props.setOmitComments(true);
		props.setTransResCharsToNCR(true);
		
		props.setTranslateSpecialEntities(true);
		
		props.setAdvancedXmlEscape(false);
		
		

		
		try{
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
			
			newPathname += "/cleaned";
			File newPath = new File(newPathname);
			if(!newPath.exists()){
				newPath.mkdirs();
			}
			
			String finalFilename = newPathname+newFilename;
			
			System.out.println(finalFilename);
			
//			File outputFile = new File(finalFilename);
//			if(!outputFile.exists()){
//				if(!outputFile.createNewFile()){
//					System.err.println("Dateifehler");
//				}
//			}else{
//				if(!outputFile.delete()){
//					System.err.println("Dateifehler");
//				}
//				if(!outputFile.createNewFile()){
//					System.err.println("Dateifehler");
//				}
//
//			}
			
//			FileOutputStream fos = new FileOutputStream(outputFile);
			
//			PrintWriter pw = new PrintWriter(fos);
//			pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<body>\n");
//			pw.flush();
//			pw.close();
			

//			pw = new PrintWriter(fos);
//			pw.write("</body>\n");
//			pw.flush();
//			pw.close();
			
			
			
			
			fis.close();
			
			htmlSerializer.writeToFile(tagNode, finalFilename, "utf-8");
			

			

		
		}catch(IOException e){
			e.printStackTrace();
		}

		
	}
	
	public void cleanAllFiles(String _path){
		
		System.out.println(_path);
		
		File f = new File(_path);
		
		if(f.isDirectory() && f.exists()){
			String[] files = f.list();
			
			for(int i=0; i<files.length; i++){
				
				File o = new File(_path+files[i]);
				if(o.isDirectory() && !files[i].contains("cleaned") && !_path.contains("cleaned")){
					
					cleanAllFiles(_path+files[i]+"/");
				}else if(o.isFile() && !o.isHidden() && files[i].contains(".html")){
					System.out.println(files[i]);
					cleanFile(_path+files[i]+"/");
					
				}
				
			}
		}
		
	}
	
	
	public static void main (String[] args){
		new cleanHTML().cleanAllFiles("html/");
	}

}
