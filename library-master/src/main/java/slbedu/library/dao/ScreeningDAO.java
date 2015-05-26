package slbedu.library.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import slbedu.library.model.Screening;

@Singleton
public class ScreeningDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void add(Screening screening) {
	
		em.persist(screening);
	}
	
	public void remove(Long id) {
		
		Screening foundScreening = em.find(Screening.class, id);
		if(foundScreening != null) {
			
			em.remove(foundScreening);
		}
	}
	
	public List<Screening> getAllScreenings() {
		
		return em.createNamedQuery("allScreenings", Screening.class).getResultList();
	}
	
	public List<Screening> getAllScreeningsByHallId(Long hallId) {
		
		return em.createNamedQuery("allScreeningsByHallId", Screening.class).
				setParameter("id", hallId).getResultList();
	}
	
	public Screening findScreeningById(Long id) {
		
		return em.createQuery("SELECT s FROM Screening s WHERE s.id = :id", Screening.class).
				setParameter("id", id).getSingleResult();
	}
}
