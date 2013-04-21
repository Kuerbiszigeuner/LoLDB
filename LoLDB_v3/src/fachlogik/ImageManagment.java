package fachlogik;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Champs;

public class ImageManagment 
{
	private EntityManager em = Persistence.createEntityManagerFactory("LoL").createEntityManager();
	private Champ_func cf = new Champ_func();
	
	//Das Bild wird heruntergeladen und in (noch) einen seperaten Ordner gespeichert
	public void download(String imageLoc, String c_name) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File locFile = new File(System.getProperty("user.home")+"/LOLDB/pics/"+c_name+".png");
		
		if(!checkFile(locFile))
		{
			//RenderedImage img = JAI.create("url", url);
			BufferedImage img = ImageIO.read(new URL(imageLoc));
			OutputStream os;
			
			ImageIO.write(img, "png", out);
		    byte[] data = out.toByteArray();
		    os = new FileOutputStream(System.getProperty("user.home")+"/LOLDB/pics/"+c_name+".png");
			os.write(data);
			os.close();
			System.out.println(data);
		}
	}
	
	//Eine Liste von URL Strings wird zurückgeliefert
	public ArrayList<String> getImageUrl() throws IOException
	{
		Document doc;
		JSoupManagment jm = new JSoupManagment();
		int count = 0;
		ArrayList<String> urllist = new ArrayList<String>();
		
		doc = jm.setSite();
		
		Elements link = doc.select("div.champ-name");
		Elements link_2 = doc.select("div#browse-build a img");
		
		//Es werden zwei Schleifen benötigt
		//Die erste geht alle möglichen Linkblöcke durch
		//Die zweite nur die die für die Bilder nötig sind
		for ( @SuppressWarnings("unused") Element ele : link )
		{
			//Wird benötigt um unnötige Links zu entfernen
			//Nach jedem Durchlauf muss die URL vom Champion entfernt werden
			//Die nächsten zwei URLs sind Icons von Influence/Riot Points
			if (count > 0 )
			{
				link_2.remove(0);
				link_2.remove(0);
				link_2.remove(0);
			}
			for ( Element ele2 : link_2 )
			{
				//Es wird gecheckt ob die URLs überhaupt Champions sind
				//Falls ja, werden Sie der Liste hinzugefügt und die ele2 Schleife abgebrochen
				if(ele2.attr("src").contains("champion") && count < link.size())
				{
					String imageLoc = ele2.absUrl("src");
					urllist.add(imageLoc);
					count++;
					break;
				}
			}
		}
		return urllist;
	}
	
	//Der Pfad des Bildes wird in die Datenbank gespeichert
	public void addImageToChamp()
	{
		int count = 0;
		em.getTransaction().begin();
		
		File ordner = getFileImageOrdner();
		if ( checkOrdner(ordner) )
		{
			File[] fileListe = fileList(ordner);
			ArrayList<String> abc = new ArrayList<String>();
			
			for ( File f : fileListe)
				abc.add(f.toString());
			
			for (Champs ca : cf.getAllChamp_DB())
			{
				if ( count > 0 )
					abc.remove(0);
				
				for ( String abcd : abc)
				{
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
	
	
	
	
	
	//Gibt ein File Array mit den beinhaltenden Dateien des übergebenen Ordners zurück
	public File[] fileList(File ordner)
	{
		File[] ff = ordner.listFiles();
		return ff;
	}
	
	
	//Gibt den Pfad der Bilder als File zurück
	public File getFileImageOrdner()
	{
		return new File(System.getProperty("user.home")+"/LOLDB/pics");
	}
	
	//Gibt den Pfad der Bilder als File zurück
	public ArrayList<String> getStringImages()
	{
		ArrayList<String> images = new ArrayList<String>();
		File ImgLoc = getFileImageOrdner();
		for ( File f : fileList(ImgLoc))
			images.add(f.toString());
			
		return images;
	}
	
	public Boolean checkFile(File f)
	{	
		boolean bool = false;
		
		String fs = f.toString();
		ArrayList<String> ilist = new ArrayList<String>(getStringImages());
		
		if ( ilist.contains(fs) )
			bool = true;
		
		return bool;
	}
	
	//Sind die besagten Ordner nicht vorhanden, so werden diese erstellt
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
