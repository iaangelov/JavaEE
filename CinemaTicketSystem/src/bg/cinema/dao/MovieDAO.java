package bg.cinema.dao;

import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import bg.cinema.models.Movie;

@Singleton
public class MovieDAO {

	@PersistenceContext
	private EntityManager em;
	
	public Collection<Movie> getAllMovies() {
		return em.createNamedQuery("getAllMovies", Movie.class).getResultList();
	}
	
	public Movie findById(Long Id) {
		return em.find(Movie.class, Id);
	}
}
