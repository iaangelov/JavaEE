package slbedu.library.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import slbedu.library.dao.HallDAO;
import slbedu.library.dao.MovieDAO;
import slbedu.library.dao.ScreeningDAO;
import slbedu.library.dao.TicketDAO;
import slbedu.library.dao.UserDAO;
import slbedu.library.model.Hall;
import slbedu.library.model.Movie;
import slbedu.library.model.Screening;
import slbedu.library.model.Ticket;
import slbedu.library.model.User;

@Stateless
public class DatabaseUtils {
    
    private static User[] Users = {
        new User("username1", "password", "email@email", new UserType(UserType.CUSTOMER)),
        new User("username2", "passwor2d", "em2ail@email", new UserType(UserType.CUSTOMER)),
        new User("user2", "password2", "mail@mail", new UserType(UserType.ADMINISTRATOR))
    };

    private static Movie[] Movies= {
        new Movie("asd", 1999, "horror"),
        new Movie("asasd", 1999, "horror"),
        new Movie("qwe", 2000, "aalsld")};
    
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
        new Ticket(Screenings[0], Users[0], 1, new SeatStatus(SeatStatus.AVAILABLE)),
        new Ticket(Screenings[0], Users[0], 2, new SeatStatus(SeatStatus.RESERVED)),
        new Ticket(Screenings[0], Users[0], 3, new SeatStatus(SeatStatus.TAKEN)),
        new Ticket(Screenings[1], Users[1], 4, new SeatStatus(SeatStatus.RESERVED)),
        new Ticket(Screenings[1], Users[1], 5, new SeatStatus(SeatStatus.TAKEN)),
        new Ticket(Screenings[1], Users[1], 6, new SeatStatus(SeatStatus.AVAILABLE)),
        new Ticket(Screenings[2], Users[2], 7, new SeatStatus(SeatStatus.AVAILABLE)),
        new Ticket(Screenings[2], Users[2], 8, new SeatStatus(SeatStatus.RESERVED)),
        new Ticket(Screenings[2], Users[2], 9, new SeatStatus(SeatStatus.TAKEN)),
        new Ticket(Screenings[2], Users[0], 10, new SeatStatus(SeatStatus.AVAILABLE)),
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
    private ScreeningDAO screeningDAO;
    
    @EJB
    private HallDAO hallDAO;
    
    @EJB
    private TicketDAO ticketDAO;
    
    public void addTestDataToDB() {
        deleteData();
        addTestUsers();
        addTestMovies();
        addTestScreenings();
        addTestTickets();
        addTestHalls();
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
    
    private void addTestHalls() {
        for (int i = 0; i < Math.min(Halls.length, allTickets.length); i++) {
            hallDAO.addSeatInHall((List<Ticket>)allTickets[i], Halls[i]);
            hallDAO.add(Halls[i]);
        }
    }
    
    private void addTestScreenings() {
        
        for (Screening screening : Screenings) {
            
            screeningDAO.add(screening);
        }
    }

    private void addTestUsers() {
        for (User user : Users) {
            userDAO.addUser(user);
        }
    }

    private void addTestTickets(){
        for(Ticket ticket : Tickets){
            ticketDAO.addTicket(ticket);
        }
    }
}
