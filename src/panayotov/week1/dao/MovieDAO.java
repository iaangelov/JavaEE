package panayotov.week1.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.persistence.Query;

import panayotov.week1.models.Movie;

@Singleton
public class MovieDAO extends GenericDAOImpl<Movie> {

	public Movie findByName(String name) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("name", name);
		return getSignleResultWithNamedQuery(QueryNames.Movie_FindByName, parameters);
	}

	public Collection<Movie> getAllMovies() {
		return getListWithNamedQuery(QueryNames.Movie_GetAllMovies);
	}

	public Long getMaxID() {
		Query query = em.createQuery("SELECT u.id FROM Movie u ORDER BY u.id DESC");
		query.setMaxResults(1);
		return (Long) query.getSingleResult();
	}
}
