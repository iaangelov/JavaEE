package morghulis.valar.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import morghulis.valar.model.Screening;

@Singleton
public class ScreeningDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void add(Screening screening) {
	
		Screening foundScreening = em.find(Screening.class, screening.getHall().getHallNumber());
		if(foundScreening == null) {
			em.persist(screening);
		}
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
	
	public List<Screening> getAllScreeningsByHallNumber(Long hallNumber) {
		
		return em.createNamedQuery("allScreeningsByHallNumber", Screening.class).
				setParameter("hallNumber", hallNumber).getResultList();
	}
	
	public Screening findScreeningById(Long id) {
		
		return em.createNamedQuery("SELECT s FROM Screening s WHERE s.id = :id", Screening.class).
				setParameter("id", id).getSingleResult();
	}
}
