package fachlogik;

import java.awt.image.RenderedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.*;

public class DataMining_Champs extends Thread implements Runnable
{
	
	private Champ_func cf = new Champ_func();
	private Typ_func tf = new Typ_func();
	private Tagging_func tgf = new Tagging_func();
	
	private EntityManager em;
	private Elements link;
	private Document doc;
	private Champs c;
	private Typ t;
	private Tagging tg;
	
	private InputStream is;
	private OutputStream os;
	
	private ArrayList<String> champion_list_HTML;
	private ArrayList<String> champion_list_DB;
	private ArrayList<String> champion_list_diff;
	
	private ArrayList<String> tag_list_HTML;
	private ArrayList<String> tag_list_DB;
	private ArrayList<String> tag_list_diff;
	
	@SuppressWarnings("unused")
	private ArrayList<Object> completeChamp_list_HTML;
	@SuppressWarnings("unused")
	private ArrayList<Object> completeChamp_list_DB;
	@SuppressWarnings("unused")
	private ArrayList<Object> completeChamp_list_diff;
	
	public DataMining_Champs()
	{
		//EntityManager wird erstellt
		setEm();
		
		//HTML Seite wird aufgerufen
		setSite();
		
		//Arraylisten werden befüllt
		getChampList_HTML();
		champion_list_DB = cf.getChampList_DB();
		
		getTypList_HTML();
		tag_list_DB = tf.getTypList_DB();
		
		if ( !cf.getFirstChamp().isEmpty() && !tf.getFirstTyp().isEmpty() )
		{
			completeChamp_list_HTML = tgf.getCompleteTagList_HTML(doc, tag_list_HTML, champion_list_HTML);
			completeChamp_list_DB = tgf.getCompleteTagList_DB();
		}	
	}
	
	public void setEm(){em = Persistence.createEntityManagerFactory("LoL").createEntityManager();}
	
	public void setSite()
	{	try{doc = Jsoup.connect("http://www.mobafire.com/league-of-legends/champions").timeout(10*2000).get();}
	
		catch (IOException e){System.out.println("Seite konnte nicht aufgerufen werden!");}}
	
	
	//Eine Liste von Daten werden aus HTML Seiten erstellt
	public void getChampList_HTML()
	{
		link = doc.select("div.champ-name");
		champion_list_HTML = new ArrayList<String>();
		
		for ( Element ele : link )
			champion_list_HTML.add(ele.text().toString());
	}
	
	public void getTypList_HTML()
	{
		link = doc.select("div.self-clear optgroup option");
		tag_list_HTML = new ArrayList<String>();
		
		for ( Element ele : link )
			tag_list_HTML.add(ele.text().toString());
	}
	
	//Füllt die Datenbank mit allen Helden
	//Wird nur ausgeführt wenn die DB leer ist via updateDB()
	
	//Parameter:
	//				Champ: updatet Champions
	//				Typ: updatet Tags
	//				Tag: updatet Tags die zum Champion gehören
	public void fillDB(String s) throws IOException
	{
		
		if(s == "Champ")
		{
			
			link = doc.select("div.champ-name");
			Elements link_2 = doc.select("div#browse-build a img");
			int count = 0;
			
			URL a = new URL("http://www.mobafire.com/league-of-legends/champions");
			URLConnection urlc = a.openConnection();
			is = urlc.getInputStream();

			//Es wird eine Liste mit allen auf Championselect aufgeführten Champions erstellt
			em.getTransaction().begin();
			for ( Element ele : link )
			{
				if (count > 0 )
				{
					link_2.remove(0);
					link_2.remove(0);
					link_2.remove(0);
				}
				c = new Champs();
				c.setName(ele.text().toString());
				
				//	Bilder herunterladen
				for ( Element ele2 : link_2 )
				{
					if(ele2.attr("src").contains("champion") && count < link.size())
					{
						
						String imageLoc = ele2.absUrl("src");
						//dmi = new DataMining_Images();
						//dmi.run();
						//c.setIcon(writingImage_ALL(imageLoc, ele.text()));
						writingImage(imageLoc, ele.text());
						count++;
						break;
					}
				}
				
				
				em.persist(c);
			}
			em.getTransaction().commit();
			is.close();
			champion_list_DB = cf.getChampList_DB();
			
		}
		
		if(s == "Typ")
		{
			link = doc.select("div.self-clear optgroup option");

			tag_list_HTML = new ArrayList<String>();
			
			em.getTransaction().begin();
			for ( Element ele : link )
			{
				tag_list_HTML.add(ele.text().toString());
				t = new Typ();
				t.setBezeichnung(ele.text().toString());
				em.persist(t);
			}
			em.getTransaction().commit();
			tag_list_DB = tf.getTypList_DB();
		}
		
		if(s == "Tag")
		{
			link = doc.select("div#browse-build a");
			String s_toLow;
			
			int count_champ = 0;
			int count_tag = 0;
			
			ArrayList<Object> champ_id = new ArrayList<Object>();
			ArrayList<Object> tag_id = new ArrayList<Object>();
			
			for ( Champs c_id : cf.getAllChamp_DB() )
				champ_id.add(c_id);
			for ( Typ t_id : tf.getAllTyp_DB() )
				tag_id.add(t_id);
			
			
			Collections.sort(tag_list_HTML);
			int count_size = champion_list_HTML.size() - champ_id.size();
			
			em.getTransaction().begin();
			for (Element ele : link)
			{
				for ( String s_tag : tag_list_HTML)
				{
					tg = new Tagging();
					
					s_toLow = s_tag.toLowerCase();
					if (ele.attr("class").contains(s_toLow) == true && count_champ <= champion_list_HTML.size()-count_size-1)
					{
						tg.setChamp((Champs) champ_id.get(count_champ));
						tg.setTyp((Typ) tag_id.get(count_tag));
						em.persist(tg);
					}
					count_tag++;
				}
				count_tag = 0;
				count_champ++;	
			}
			em.getTransaction().commit();
		}
	}

	//Die DB wird komplett erneuert wenn kein Champ vorhanden ist
	//Falls einer vorhanden ist, wird geprüft ob die Anzahl der Listen gleich sind
	//Sind diese ungleich so wird die DB geupdatet auf Basis der fehlenden Champs
	public String updateDB() throws IOException
	{	
		String message = null;
		champion_list_DB = cf.getChampList_DB();
		tag_list_DB = tf.getTypList_DB();
			
		//champion_list_diff wird mit dem gleichen Inhalt wie champion_list_HTML befüllt
		//danach werden die in der DB enthaltenen Stringwerte subtrahiert
		//es wird eine List mit fehlenden Champions erstellt
		champion_list_diff = new ArrayList<String>(champion_list_HTML);
		champion_list_diff.removeAll(champion_list_DB);
		
		tag_list_diff = new ArrayList<String>(tag_list_HTML);
		tag_list_diff.removeAll(tag_list_DB);
		
		//DB wird geupdatet wenn die HTML und DB Liste ungleich sind
		////////////////////// Champion Aktualisierung //////////////////////
		if (champion_list_HTML.size() != champion_list_DB.size())
		{
			em.getTransaction().begin();
			for ( String s : champion_list_diff )
			{
				//imgSRC = getIMG_src_diff(s);
				c = new Champs();
				c.setName(s);
				//c.setIcon(writingImage_ALL(imgSRC.toString(), champion_list_diff.get(count), is));
				em.persist(c);
				
				message = message + s;
			}
			em.getTransaction().commit();
		}
		
		////////////////////// TYPEN Aktualisierung //////////////////////
		if (tag_list_HTML.size() != tag_list_DB.size())
		{
			em.getTransaction().begin();
			for ( String s : tag_list_diff )
			{
				t = new Typ();
				t.setBezeichnung(s);
				em.persist(t);
				message = message + s;
			}
			em.getTransaction().commit();
		}

		//Veränderungen an den Tags ist anders nicht möglich
		//Die Tabelle muss neu geschrieben werden
		em.getTransaction().begin();
		em.createNativeQuery("truncate table tagging").executeUpdate();
		em.getTransaction().commit();
		fillDB("Tag");
		
		fillImage();
		return message;
	}

	//Bilder zu den jeweiligen Champions werden geladen und als BLOB in die Datenbank gespeichert
	public void writingImage(final String imageLoc, final String c_name) throws MalformedURLException, IOException 
	{
		URL url = new URL(imageLoc);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final RenderedImage img = JAI.create("url", url);
		
		new Thread()
		{
			public void run()
			{
				try
				{ 
				    ImageIO.write(img, "png", out);
				    byte[] data = out.toByteArray();
				    os = new FileOutputStream(System.getProperty("user.home")+"/Desktop/LOLDB/pics/"+c_name+".png");
					os.write(data);
					os.close();
					System.out.println(data);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();	
	}
	
	
	public void fillImage()
	{
		int count = 0;
		em.getTransaction().begin();
		
		File ordner = new File(System.getProperty("user.home")+"/Desktop/LOLDB/pics");
		if ( checkOrdner(ordner) )
		{
			File[] fileListe = ordner.listFiles();
			ArrayList<String> abc = new ArrayList<String>();
			
			for ( File f : fileListe)
				abc.add(f.toString());
			
			for (Champs ca : cf.getAllChamp_DB())
			{
				if ( count > 0 )
					abc.remove(0);
				
				for ( String abcd : abc)
				{
					//c.setName(ca.getName());
					//ca.setName(ca.getName());
					ca.setPath(abcd.toString());
					
					em.merge(ca);
					
					count++;
					break;
				}
			}
			em.getTransaction().commit();
		}
		else
		{
			System.out.println("YO");
		}
	}
	
	
	public boolean checkOrdner(File ordner)
	{
		boolean zeug = false;
		
		if ( ordner.exists() )
			zeug = true;
		else
			ordner.mkdirs();
		
		
		return zeug;
	}
}
