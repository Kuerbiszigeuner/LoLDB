package fachlogik;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.Champs;

public class Champ_func 
{
	private EntityManager em = Persistence.createEntityManagerFactory("LoL").createEntityManager();
	
	public List<Champs> getAllChamp_DB()
	{
		Query q = em.createNamedQuery("getAllChamps");
		
		@SuppressWarnings("unchecked")
		List<Champs> liste = q.getResultList();
		
		return liste;
	}
	
	public List<Champs> getFirstChamp()
	{
		Query q = em.createNamedQuery("getFirstChamp");
		
		@SuppressWarnings("unchecked")
		List<Champs> liste = q.getResultList();
		
		return liste;
	}
	
	public List<Champs> getParamChamp_DB(String s)
	{
		Query q = em.createNamedQuery("getParamChamp");
		q.setParameter("p", s);
		
		@SuppressWarnings("unchecked")
		List<Champs> liste = q.getResultList();
		
		return liste;
	}
	
	public List<Champs> getChamp_DB(String s)
	{
		Query q = em.createNamedQuery("getChamp");
		q.setParameter("p", s);
		
		@SuppressWarnings("unchecked")
		List<Champs> liste = q.getResultList();
		
		return liste;
	}
	
	
	public ArrayList<String> getChampList_DB()
	{
		ArrayList<String> champion_list_DB = new ArrayList<String>();
		for ( Champs c : getAllChamp_DB())
			champion_list_DB.add(c.toString());
		return champion_list_DB;
	}
	
	//Ein Champion wird gelöscht, mitsamt seinen Tags
	public void deleteChamp(Champs c, ArrayList<Object> abc)
	{
		em.getTransaction().begin();
		
		for ( Object o : abc)
			em.remove(em.merge(o));
		em.remove(em.merge(c));
		em.getTransaction().commit();
	}
	
}
