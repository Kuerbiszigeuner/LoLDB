package fachlogik;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupManagment 
{
	private Document doc;
	
	public Document setSite()
	{	
		try
		{
			doc = Jsoup.connect("http://www.mobafire.com/league-of-legends/champions").timeout(10*2000).get();
			return doc;
		}
		catch (IOException e)
		{
			System.out.println("Seite konnte nicht aufgerufen werden!");
		}
		return null;
	}
	
	
	public Document setSite(String site)
	{	
		try
		{
			doc = Jsoup.connect(site).timeout(10*2000).get();
			return doc;
		}
		catch (IOException e)
		{
			System.out.println("Seite konnte nicht aufgerufen werden!");
		}
		return null;
	}
}
