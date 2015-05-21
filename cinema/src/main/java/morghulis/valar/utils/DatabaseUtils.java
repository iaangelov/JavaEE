package morghulis.valar.utils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import morghulis.valar.dao.MovieDAO;
import morghulis.valar.model.Movie;

@Stateless
public class DatabaseUtils {

    private static Movie[] Movies= {
        new Movie("asd", 1999, "horror"),
        new Movie("qwe", 2000, "aalsld")};

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private MovieDAO movieDAO;
    
    public void addTestDataToDB(){
    	deleteData();
    	addTestMovies();
    }
    
    private void deleteData() {
        em.createQuery("DELETE FROM Movie").executeUpdate();
   }

    private void addTestMovies() {
        for (Movie movie: Movies) {
            movieDAO.addMovie(movie);
        }
    }

}
