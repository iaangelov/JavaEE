package morghulis.valar.dao;

import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import morghulis.valar.model.Movie;

@Singleton
public class MovieDAO {

	@PersistenceContext
	private EntityManager em;

	public void addMovie(Movie movie) {
		Movie m = em.find(Movie.class, movie.getId());
		if (m == null) {
			em.persist(movie);
		}
	}

	public void deleteMovie(long id) {
		Movie movieToRemove = em.find(Movie.class, id);
		if (movieToRemove != null) {
			em.remove(movieToRemove);
		}
	}

	public Movie findById(long key) {
		return em.find(Movie.class, key);
	}

	public Movie findByName(String name) {
		TypedQuery<Movie> query = em
				.createNamedQuery("findByName", Movie.class).setParameter(
						"movieName", name);
		return queryMovie(query);
	}

	public Collection<Movie> getAllMovies() {
		return em.createNamedQuery("getAllMovies", Movie.class).getResultList();
	}

	private Movie queryMovie(TypedQuery<Movie> query) {
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
