package morghulis.valar.dao;

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
}
