package importFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;

import Measuring.Measure;

public class TDaR {
	
	static int outCounter = 0;
	
	public static void main(String[] args){
		
		if(args.length==2){
			String file = args[0];
			String trecWeb = args[1];
			System.out.println("trecweb: "+trecWeb);
			
//			System.out.println("delete html folder");
//			delete(new File("html"));
//			
			ArrayList<String> folders = readFolders(file);
//			System.out.println("read files from trecweb");
//			new extractHTML(trecWeb,folders);
//			
//			System.out.println("clean all files");
//			new cleanHTML().cleanAllFiles("html/");
			
			
			FileOutputStream fos = null;
			PrintWriter bw = null;
			try{
				File ex = new File("export.trecweb");
				ex.createNewFile();
				fos = new FileOutputStream(ex);
				bw = new PrintWriter(fos);
			
			}catch(IOException e){
				e.printStackTrace();
			}
			
			System.out.println("process files");
			for( String h : folders ){
				Measure m = new Measure(h);

				exportToTrecweb(bw,fos,m.getDc().getContentDoms());
			}
			
			try {
				bw.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			System.err.println("Das Programm braucht zwei Parameter die Datei mit den Domains und die TrecWeb");
		}
		
		
		
		
		
		
	}
	
	private static ArrayList<String> readFolders(String _f){
		ArrayList<String> folders = new ArrayList<String>();
		File f = new File(_f);
		if(f.exists() && f.isFile()){
			try{
				FileReader fr = new FileReader(f);
				BufferedReader reader = new BufferedReader(fr);
				String line;
				
				while((line = reader.readLine()) != null){
					folders.add(line);
				}
				
				reader.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			System.err.println(_f+" ist keine Datei!");
		}
		
		return folders;
	}
	
	private static void delete(File f) {
		  if (f.exists() && f.isDirectory()) {
		    for (File c : f.listFiles())
		      delete(c);
		  }
		  if (f.exists() && !f.delete())
			try {
				throw new FileNotFoundException("Failed to delete file: " + f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private static void exportToTrecweb(PrintWriter bw, FileOutputStream fos, ArrayList<Node> trees){
		for(Node n : trees){
			
			
			bw.println("\n<DOC>\n<DOCNO>"+(outCounter++)+"</DOCNO>");
			bw.flush();
			
			try {
				DomCrawler.printDocument(n, fos);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
			
			bw = new PrintWriter(fos);
			bw.println("</DOC>");
			bw.flush();
			
		}
	}

}
