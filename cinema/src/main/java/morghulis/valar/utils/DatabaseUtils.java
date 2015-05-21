package morghulis.valar.utils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import morghulis.valar.dao.MovieDAO;
import morghulis.valar.dao.UserDAO;
import morghulis.valar.model.Movie;
import morghulis.valar.model.User;

@Stateless
public class DatabaseUtils {

    private static Movie[] Movies= {
        new Movie("asd", 1999, "horror"),
        new Movie("qwe", 2000, "aalsld")};
    
    private static User[] USERS = {
    	new User("username", "password", "email@email", new UserType(UserType.CUSTOMER)),
    	new User("user2", "password2", "mail@mail", new UserType(UserType.ADMINISTRATOR))
    };
   
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private MovieDAO movieDAO;
    
    @EJB
    private UserDAO userDAO;
    
    public void addTestDataToDB(){
    	deleteData();
    	addTestMovies();
    	addTestUsers();
    }
    
    private void deleteData() {
        em.createQuery("DELETE FROM Movie").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
   }

    private void addTestMovies() {
        for (Movie movie: Movies) {
            movieDAO.addMovie(movie);
        }
    }
    
    private void addTestUsers(){
    	for(User user : USERS){
    		userDAO.addUser(user);
    	}
    }

}
