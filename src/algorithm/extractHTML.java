package algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class extractHTML {

	/**
	 * input file
	 * */
	String trecLocation = "html/0.trecweb";
	
	/**
	 * output location for separated files in folders named by there uri
	 * */
	String outLocation = "html/";
	
	ArrayList<String> myDocs = null;
	

	
	public extractHTML(String _trecLoc, ArrayList<String> docs) {
		trecLocation = _trecLoc;
		myDocs = docs;
		
		read();
	}
	
	public void read() {
			
			BufferedReader reader = null;
			int docCtr = 0;
			try {
				reader = new BufferedReader( new InputStreamReader( new FileInputStream( new File(  this.trecLocation ) ) ) );
				
				String line = "";
				String docTxt = "";
				String docTxtOut = "";
				boolean test = false;
				
				while( ( line = reader.readLine() ) != null ) {
					
					
					//add lines to output
					if( !line.isEmpty() ) {
							docTxt += "\n" + line;
							
							//start of html doc
							if(!test && line.contains("<html")){
								test = true;
							}
							
							//end of html doc
							if(line.contains("</html>")){
								test = false;
								docTxtOut += "\n" + line;
							}
							
							//add line to html output
							if(test){
								docTxtOut += "\n" + line;
							}
					}
					
					//end of Trecweb element -> output file
					if( line.contains( "</DOC>" ) ) {
						
						docTxt += line;
						String dir = "";
						
						//get domain name from trecweb header
						if( ( dir = analyseHdr( docTxt ) ) != null ) {
							
							Writer writer = null;
							
							try{
								File f = new File(this.outLocation + dir ); 
								//create domain dir
								if( !( f.isDirectory() ) ) {
									f.mkdirs();
								}
								//create output file
								writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File( 
										( this.outLocation + dir + "/" + docCtr + ".html" ) ) ) ) );
								docCtr++;
								writer.write( docTxtOut );
								
								
							} catch( IOException ioe ) { 
								ioe.printStackTrace(); 
							} finally{
								try{
									writer.close();
								} catch( IOException ioe ) { ioe.printStackTrace();}
							}
						}
						
						
						//reset outputs
						docTxt = "";
						docTxtOut = "";
						test = false;
					}
				}
				
				
				
			} catch( IOException ioe ) {
				ioe.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	private String analyseHdr( String _docTxt ) {

		String hdr = _docTxt.substring( _docTxt.indexOf("<DOCHDR>"), _docTxt.indexOf( "</DOCHDR>" ) );

		
		//return url from URL List if the hdr contains it
		for( String url : this.myDocs ) {
			if( hdr.contains( url ) ) {
				return url;
			}
		}
		
		//document is not in List -> dont output
		return null;
	}
	

}
