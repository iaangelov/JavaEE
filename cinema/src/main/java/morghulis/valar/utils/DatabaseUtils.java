package morghulis.valar.utils;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import morghulis.valar.dao.HallDAO;
import morghulis.valar.dao.MovieDAO;
import morghulis.valar.dao.ScreeningDAO;
import morghulis.valar.dao.UserDAO;
import morghulis.valar.model.Hall;
import morghulis.valar.model.Movie;
import morghulis.valar.model.Screening;
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
   
    private static Hall[] Halls = {
    	new Hall(123), 
    	new Hall(124),
    	new Hall(125)
    };
    
    private static Screening[] Screenings = {
    	new Screening(Halls[0], Movies[0], new Date()),
    	new Screening(Halls[1], Movies[1], new Date()),
    	new Screening(Halls[2], Movies[1], new Date())
    };
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private MovieDAO movieDAO;
    
    @EJB
    private UserDAO userDAO;
    
    @EJB
    private HallDAO hallDAO;
    
    @EJB
    private ScreeningDAO screeningDAO;
    
    public void addTestDataToDB(){
    	deleteData();
    	addTestMovies();
    	addTestUsers();
    	addTestHalls();
    	addTestScreenings();
    }
    
    private void addTestScreenings() {
		
		for (Screening screening : Screenings) {
			
			screeningDAO.add(screening);
		}
	}

	private void deleteData() {
        em.createQuery("DELETE FROM Movie").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.createQuery("DELETE FROM Hall").executeUpdate();
        em.createQuery("DELETE FROM Screening").executeUpdate();
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
    
    private void addTestHalls() {
    	
    	for (Hall hall : Halls) {
			
    		hallDAO.add(hall);
		}
    }

}
