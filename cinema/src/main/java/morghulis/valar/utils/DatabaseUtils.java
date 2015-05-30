package morghulis.valar.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import morghulis.valar.dao.HallDAO;
import morghulis.valar.dao.MovieDAO;
import morghulis.valar.dao.ScreeningDAO;
import morghulis.valar.dao.TicketDAO;
import morghulis.valar.dao.UserDAO;
import morghulis.valar.model.Hall;
import morghulis.valar.model.Movie;
import morghulis.valar.model.Screening;
import morghulis.valar.model.Ticket;
import morghulis.valar.model.User;

@Stateless
public class DatabaseUtils {

    private static Movie[] Movies= {
        new Movie("asd", 1999, "horror"),
        new Movie("qwe", 2000, "aalsld")};
    
    private static User[] USERS = {
    	new User("user", "user", "email@email", UserType.CUSTOMER),
    	new User("user2", "user2", "email2@email", UserType.CUSTOMER),
    	new User("admin", "admin", "mail@mail", UserType.ADMINISTRATOR)
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
    
    private static Ticket[] Tickets = {
        new Ticket(Screenings[0], USERS[0], 1, SeatStatus.AVAILABLE),
        new Ticket(Screenings[0], USERS[0], 2, SeatStatus.RESERVED),
        new Ticket(Screenings[0], USERS[0], 3, SeatStatus.TAKEN),
        new Ticket(Screenings[1], USERS[1], 4, SeatStatus.RESERVED),
        new Ticket(Screenings[1], USERS[1], 5, SeatStatus.TAKEN),
        new Ticket(Screenings[1], USERS[1], 6, SeatStatus.AVAILABLE),
        new Ticket(Screenings[2], USERS[2], 7, SeatStatus.AVAILABLE),
        new Ticket(Screenings[2], USERS[2], 8, SeatStatus.RESERVED),
        new Ticket(Screenings[2], USERS[2], 9, SeatStatus.TAKEN),
        new Ticket(Screenings[2], USERS[0], 10,SeatStatus.AVAILABLE)
    };
   
    private static List<Ticket> ticketsForHall0 = Arrays.asList(Tickets[0],Tickets[1],Tickets[2]);
    private static List<Ticket> ticketsForHall1 = Arrays.asList(Tickets[3],Tickets[4],Tickets[5]);
    private static List<Ticket> ticketsForHall2 = Arrays.asList(Tickets[6],Tickets[7],Tickets[8],Tickets[9]);
    
    private static Object[] allTickets = {ticketsForHall0, ticketsForHall1, ticketsForHall2};
    
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

    @EJB
    private TicketDAO ticketDAO;
    
    public void addTestDataToDB(){
    	deleteData();
    	addTestMovies();
    	addTestUsers();
    	addTestHalls();
    	addTestScreenings();
    	addTestTickets();
    	
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
        em.createQuery("DELETE FROM Ticket").executeUpdate();
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

    private void addTestTickets(){
        for(Ticket ticket : Tickets){
            ticketDAO.addTicket(ticket);
        }
    }
}
