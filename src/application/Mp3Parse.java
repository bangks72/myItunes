package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.LyricsHandler;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

public class Mp3Parse {
	
	File myfile;
	
	public Mp3Parse(File file){
		myfile = file;
	}
	
	public Audio Parse() throws Exception, IOException, SAXException, TikaException {
        //detecting the file type
        BodyContentHandler handler = new BodyContentHandler();
	    Metadata metadata = new Metadata();
	    FileInputStream inputstream = new FileInputStream(myfile);
	    ParseContext pcontext = new ParseContext();
	      
	    //Mp3 parser
	    Mp3Parser  Mp3Parser = new  Mp3Parser();
	    Mp3Parser.parse(inputstream, handler, metadata, pcontext);
	    LyricsHandler lyrics = new LyricsHandler(inputstream,handler);
	      
	    while(lyrics.hasLyrics()) {
	    	System.out.println(lyrics.toString());
	    }
	      
	    System.out.println("Contents of the document:" + handler.toString());
	    System.out.println("Metadata of the document:");
	    String[] metadataNames = metadata.names();
	
	    for(String name : metadataNames) {		        
	    	System.out.println(name + ": " + metadata.get(name));
	    }
	    System.out.println(metadata.get("xmpDM:album"));
	    Audio aud = new Audio(myfile.getName(), metadata.get("xmpDM:artist"), metadata.get("xmpDM:album"), metadata.get("xmpDM:genre"), myfile.getAbsolutePath());
	    return aud;
	 }
}