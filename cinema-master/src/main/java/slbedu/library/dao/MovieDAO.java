package slbedu.library.dao;

import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import slbedu.library.model.Movie;

@Singleton
public class MovieDAO {

    @PersistenceContext
    private EntityManager em;

    public void addMovie(Movie movie) {
        em.persist(movie);
    }

    public void deleteMovie(Movie movieToRemove) {
        if (movieToRemove != null) {
            em.remove(movieToRemove);
        }
    }

    public Movie findById(long key) {
        return em.find(Movie.class, key);
    }

    public Movie findByName(String name) {
        TypedQuery<Movie> query = em
                .createNamedQuery("findByName", Movie.class)
                .setParameter(
                        "name", name);
        return queryMovie(query);
    }

    public Collection<Movie> getAllMovies() {
        return em.createNamedQuery("getAllMovies", Movie.class).getResultList();
    }

    private Movie queryMovie(TypedQuery<Movie> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
