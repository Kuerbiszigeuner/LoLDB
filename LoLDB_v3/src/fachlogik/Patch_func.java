package fachlogik;

import java.util.List;

import javax.persistence.*;

import entities.Patch;

public class Patch_func 
{
	private EntityManager em = Persistence.createEntityManagerFactory("LoL").createEntityManager();
	
	
	
	public List<Patch> getAllPatch_DB()
	{
		Query q = em.createNamedQuery("getAllPatches");
		
		@SuppressWarnings("unchecked")
		List<Patch> liste = q.getResultList();
		
		return liste;
	}
	
	
	//---------TEST-------
	//L�scht den �bergebenen Patch
	public void deletePatch(Patch p)
	{
		em.getTransaction().begin();
		em.remove(em.merge(p));
		em.getTransaction().commit();
	}

}