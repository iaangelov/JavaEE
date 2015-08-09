package panayotov.week1.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

import panayotov.week1.models.Movie;

@Stateful
public class MovieDAO extends GenericDAOImpl<Movie> {

	public Movie findByName(String name) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("name", name);
		return getSignleResultWithNamedQuery(QueryNames.Movie_FindByName, parameters);
	}

	public Collection<Movie> getAllMovies() {
		return getListWithNamedQuery(QueryNames.Movie_GetAllMovies);
	}
}
