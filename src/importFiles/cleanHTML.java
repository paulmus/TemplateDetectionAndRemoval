package importFiles;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.w3c.tidy.Tidy;

public class cleanHTML {
	
	
	
	public cleanHTML(){
		

	}
	
	public static void cleanFile(String _file){
		
		
		
		Tidy tidy = new Tidy();
		
		tidy.setDropFontTags(true);
		tidy.setAltText("");
		tidy.setHideComments(true);
		tidy.setMakeClean(true);		
		tidy.setSmartIndent(true);
//		tidy.setPrintBodyOnly(true);
		tidy.setHideEndTags(false);
		tidy.setForceOutput(true);
		tidy.setIndentAttributes(false);
		tidy.setDropEmptyParas(false);
		tidy.setLiteralAttribs(false);
		tidy.setShowWarnings(false);
		tidy.setXmlOut(true);
		tidy.setTrimEmptyElements(false); //important, otherwise empty elements are removed
		tidy.setInputEncoding("UTF8");
		tidy.setOutputEncoding("UTF8");
		
		

		
		try{
			File inputFile = new File(_file.substring(0, _file.length()-1));
			FileInputStream fis = new FileInputStream( inputFile );
			
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
			
			File outputFile = new File(finalFilename);
			if(!outputFile.exists()){
				if(!outputFile.createNewFile()){
					System.err.println("Dateifehler");
				}
			}else{
				if(!outputFile.delete()){
					System.err.println("Dateifehler");
				}
				if(!outputFile.createNewFile()){
					System.err.println("Dateifehler");
				}

			}
			
			FileOutputStream fos = new FileOutputStream(outputFile);
			
//			PrintWriter pw = new PrintWriter(fos);
//			pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<body>\n");
//			pw.flush();
//			pw.close();
			
			
			tidy.parse(fis, fos);
			
//			pw = new PrintWriter(fos);
//			pw.write("</body>\n");
//			pw.flush();
//			pw.close();

			
			fos.flush();
			fos.close();
			fis.close();
			

			

		
		}catch(IOException e){
			e.printStackTrace();
		}

		
	}
	
	public static void cleanAllFiles(String _path){
		
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
