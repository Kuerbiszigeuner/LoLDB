package fachlogik;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DataMining_PatchNotes 
{
	private Document doc;
	private Elements link;
	private Elements content;
	private Elements content2;
	
	public void setSite(String s)
	{	try{doc = Jsoup.connect("http://leaguepedia.com/wiki/" + s).timeout(10*2000).get();}
		catch (IOException e){System.out.println("Seite konnte nicht aufgerufen werden!");}}
	
	
	public String getPatchNotes(String s)
	{
		setSite(s);
		System.out.println("Patchnotes für: " + s);
		
		link = doc.select("div#mw-content-text");
		content = doc.getElementsByAttributeValueContaining("style", "overflow:auto;");
		String abc = content.toString();
		
		abc = abc.replace("/images/", "http://leaguepedia.com/images/");
		

		return abc;
	}
	

}
