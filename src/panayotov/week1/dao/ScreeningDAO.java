package panayotov.week1.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

import panayotov.week1.models.Screening;

@Stateful
public class ScreeningDAO extends GenericDAOImpl<Screening> {

	private Map<String, Object> parameters = new HashMap<>();

	public Collection<Screening> getAllScreenings() {
		return getListWithNamedQuery(QueryNames.Screening_GetAllScreenings);
	}

	public Collection<Screening> getAllScreeningsByHallId(Long hallId) {
		parameters.clear();
		parameters.put("id", hallId);
		return getListWithNamedQuery(
				QueryNames.Screening_GetAllScreeningsByHallID, parameters);
	}

	public Collection<Screening> getAllScreeningsByMovieName(String name) {
		parameters.clear();
		parameters.put("name", name);
		return getListWithNamedQuery(
				QueryNames.Screening_GetAllScreeningsByMovieName, parameters);
	}
}
