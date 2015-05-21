package morghulis.valar.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class TicketDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	
}
