package morghulis.valar.dao;

import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.TypedQuery;

import morghulis.valar.model.Hall;
import morghulis.valar.model.Screening;

@Singleton
public class HallDAO extends GenericDAOImpl<Hall> {

	public Collection<Hall> getAllHalls() {
		return getListWithNamedQuery(QueryNames.Hall_GetAllHalls);
	}

	public Hall findByHallNumber(int hallNumber) {
		TypedQuery<Hall> query = em.createQuery("SELECT h FROM Hall h WHERE h.hallNumber = :hallNum", Hall.class);
		query.setParameter("hallNum", hallNumber);
		return query.getSingleResult();
	}
	
}
