package panayotov.week1.dao;

import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.TypedQuery;

import panayotov.week1.models.Hall;

@Singleton
public class HallDAO extends GenericDAOImpl<Hall> {

	public Collection<Hall> getAllHalls() {
		return getListWithNamedQuery(QueryNames.Hall_GetAllHalls);
	}

	public Hall findByHallNumber(int hallNumber) {
		TypedQuery<Hall> query = em.createQuery("SELECT h FROM Hall h WHERE h.hallNumber = :hallNum", Hall.class);
		query.setParameter("hallNum", hallNumber);
		Hall res = query.getSingleResult();
		if(null == res){
			res = new Hall(hallNumber);
		}
		return res;
	}
	
}
