package morghulis.valar.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import morghulis.valar.model.Hall;

@Singleton
public class HallDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void add(Hall hall) {
		
		em.persist(hall);
	}
	
	public List<Hall> getAllHalls() {
		
		return em.createQuery("SELECT h FROM Hall h", Hall.class).getResultList();
	}
	
	public Hall findHallById(Long id){
	    return em.find(Hall.class, id);
	}
	
}
