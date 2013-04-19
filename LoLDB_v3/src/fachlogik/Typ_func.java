package fachlogik;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.Champs;
import entities.Typ;

public class Typ_func 
{
	private EntityManager em = Persistence.createEntityManagerFactory("LoL").createEntityManager();
	
	public List<Typ> getAllTyp_DB()
	{
		Query q = em.createNamedQuery("allTyp");
		
		@SuppressWarnings("unchecked")
		List<Typ> liste = q.getResultList();
		
		return liste;
	}

	
	public List<Typ> getFirstTyp()
	{
		Query q = em.createNamedQuery("firstTyp");
		
		@SuppressWarnings("unchecked")
		List<Typ> liste = q.getResultList();
		
		return liste;
	}
	
	
	
	public List<Typ> getTagForChamp(Champs c)
	{
		Query q = em.createNamedQuery("getTagForChamp");
		q.setParameter("p", c);
		
		@SuppressWarnings("unchecked")
		List<Typ> liste = q.getResultList();
		
		return liste;
	}
	
	
	
	
	public List<Typ> getParamTyp(String s)
	{
		Query q = em.createNamedQuery("getParamTyp");
		q.setParameter("p", s);
		
		@SuppressWarnings("unchecked")
		List<Typ> liste = q.getResultList();
		
		return liste;
	}
	
	
	public List<Champs> getTyp_DB(String s)
	{
		Query q = em.createNamedQuery("getTyp");
		q.setParameter("p", s);
		
		@SuppressWarnings("unchecked")
		List<Champs> liste = q.getResultList();
		
		return liste;
	}
	
	
	
	public ArrayList<String> getTypList_DB()
	{
		ArrayList<String> tag_list_DB = new ArrayList<String>();
		for ( Typ t : getAllTyp_DB())
			tag_list_DB.add(t.toString());
		return tag_list_DB;
	}
}
