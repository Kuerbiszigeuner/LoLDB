package fachlogik;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Patch;

public class PatchManagment 
{
	private Document doc;
	private JSoupManagment jsm = new JSoupManagment();
	private EntityManager em;
	private Patch p;
	private Patch_func pf = new Patch_func();
	
	private Elements link;
	private ArrayList<String>	patch_list_HTML = new ArrayList<String>();
	private ArrayList<String>	patch_list_DB = new ArrayList<String>();
	
	
	public PatchManagment()
	{
		doc = jsm.setSite("http://leagueoflegends.wikia.com/wiki/Category:Patch_notes");
		em = Persistence.createEntityManagerFactory("LoL").createEntityManager();
	}

	//Speichert alle Patchesnummern (Strings) in die Datenbank (Komplett neu)
	//Vorhandene Daten werden gelöscht
	public void getPatches()
	{
		link = doc.select("div.mw-content-ltr table tbody tr td ul li");
		
		em.getTransaction().begin();
		em.createNativeQuery("truncate table Patch").executeUpdate();
		for ( Element ele : link )
		{
			p = new Patch();
			p.setVersion(ele.text());
			em.persist(p);
		}
		em.getTransaction().commit();
		System.out.println("Patch DB neu erstellt!");
	}
	
	//Die DB wird aktualisiert, doch nicht komplett neu erstellt
	//Fehlende Patches werden hinzugefügt
	public void updatePatchDB()
	{
		patch_list_DB.clear();
		patch_list_HTML.clear();
		
		getPatchList_DB();
		getPatchList_HTML();
		
		patch_list_HTML.removeAll(patch_list_DB);
		
		em.getTransaction().begin();
		for ( String s : patch_list_HTML)
		{
			p = new Patch();
			p.setVersion(s);
			em.persist(p);
		}
		em.getTransaction().commit();
	}
	
	
	private void getPatchList_DB()
	{
		if ( pf.getAllPatch_DB() != null)
			for ( Patch p : pf.getAllPatch_DB() )
				patch_list_DB.add(p.toString());
	}

	public void getPatchList_HTML()
	{
		link = doc.select("div.mw-content-ltr table tbody tr td ul li");
		patch_list_HTML = new ArrayList<String>();
		
		for ( Element ele : link )
			patch_list_HTML.add(ele.text().toString());
	}
	
	//TODO URLS herausfiltern
	public void getPatchList_HTML_URL()
	{
		link = doc.select("div.mw-content-ltr table tbody tr td ul li");
		ArrayList<String> patch_list_HTML_URL = new ArrayList<String>();
		
		for ( Element ele : link )
			patch_list_HTML_URL.add(ele.tagName("a").toString());
	}
	
	//---------TEST-------
	//Löscht den übergebenen Patch
	public void deletePatch(Patch p)
	{
		if (p != null)
			pf.deletePatch(p);
	}


}
