package morghulis.valar.dao;

import java.util.Collection;

import javax.ejb.Singleton;

import morghulis.valar.model.Hall;

@Singleton
public class HallDAO extends GenericDAOImpl<Hall> {

	public Collection<Hall> getAllHalls() {
		return getListWithNamedQuery(QueryNames.Hall_GetAllHalls);
	}
}
