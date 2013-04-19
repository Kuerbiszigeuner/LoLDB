package fachlogik;

import java.util.*;

import javax.persistence.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Champs;
import entities.Tagging;

public class Tagging_func 
{
	private EntityManager em = Persistence.createEntityManagerFactory("LoL").createEntityManager();
	
	public List<Tagging> getAllTag_DB()
	{
		Query q = em.createNamedQuery("getAllTag");
		
		@SuppressWarnings("unchecked")
		List<Tagging> liste = q.getResultList();
		
		return liste;
	}
	
	public List<Tagging> getFirstTag()
	{
		Query q = em.createNamedQuery("firstTag");
		
		@SuppressWarnings("unchecked")
		List<Tagging> liste = q.getResultList();
		
		return liste;
	}
	
	public List<Tagging> getTaggingFromChamp(Champs c)
	{
		Query q = em.createNamedQuery("getTaggingFromChamp");
		q.setParameter("p", c);
		
		@SuppressWarnings("unchecked")
		List<Tagging> liste = q.getResultList();
		
		return liste;
	}
	
	public ArrayList<Object> getCompleteTagList_DB()
	{
		ArrayList<Object> completeChamp_list_DB = new ArrayList<Object>();
		
		//for ( Champs c : cf.getAllChamp_DB())
		//{
			
			for ( Tagging tg : getAllTag_DB() )
			{
				completeChamp_list_DB.add(tg.getChamp());
				completeChamp_list_DB.add(tg.getTyp());
			}
		//}
		return completeChamp_list_DB;
	}
	
	
	//Gibt ein Objekt zurück, welches die Championliste mit Tags von Mobafire enthält
	public ArrayList<Object> getCompleteTagList_HTML(Document doc, ArrayList<String> tag_list_HTML, ArrayList<String> champion_list_HTML)
	{
		Elements link = doc.select("div#browse-build a");
		Elements link_name = doc.select("div.champ-name");
		
		ArrayList<String> champ_html = new ArrayList<String>();
		ArrayList<Object> completeChamp_list_HTML = new ArrayList<Object>();
		
		int count_champ = 0;
		
		Collections.sort(tag_list_HTML);
		
		for ( Element ele : link_name )
			champ_html.add(ele.text());
		
		
		for ( Element ele : link)
		{
			
			for ( String s : tag_list_HTML )
			{
				String s_low = s.toLowerCase();
				if(ele.attr("class").contains(s_low) == true && count_champ<=champ_html.size())
				{	
					s_low = Character.toUpperCase(s_low.charAt(0))+s_low.substring(1);
					completeChamp_list_HTML.add(champ_html.get(count_champ));
					completeChamp_list_HTML.add(s_low);
				}
					
			}
			count_champ++;
		}
		return completeChamp_list_HTML;
	}
}
