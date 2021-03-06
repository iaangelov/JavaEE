package morghulis.valar.utils;

import java.util.Date;

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

	private static Movie[] Movies = { new Movie("Get Hard"),
			new Movie("Frozen"), new Movie("Cinderella"),
			new Movie("The Wedding Ringer"), new Movie("Beauty and the Beast"),
			new Movie("Tangled") };

	private static User[] USERS = {
			new User("user", "user", "email@email", "Pencho", "Penchev",
					UserType.CUSTOMER),
			new User("user2", "user2", "email2@email", "Ime", "Familia",
					UserType.CUSTOMER),
			new User("admin", "admin", "mail@mail", "Ime", "Familia2",
					UserType.ADMINISTRATOR) };

	private static Hall[] Halls = { new Hall(123), new Hall(124), new Hall(125) };

	private static Screening[] Screenings = {
			new Screening(Halls[0], Movies[0], new Date()),
			new Screening(Halls[1], Movies[1], new Date()),
			new Screening(Halls[2], Movies[1], new Date()),
			new Screening(Halls[2], Movies[1], new Date()),
			new Screening(Halls[2], Movies[1], new Date()),
			new Screening(Halls[2], Movies[1], new Date()),
			new Screening(Halls[2], Movies[1], new Date()) };

	private static Ticket[] Tickets = {
			new Ticket(Screenings[0], USERS[0], 244, SeatStatus.TAKEN),
			new Ticket(Screenings[0], USERS[0], 245, SeatStatus.RESERVED),
			new Ticket(Screenings[0], USERS[0], 246, SeatStatus.TAKEN),
			new Ticket(Screenings[1], USERS[1], 300, SeatStatus.RESERVED),
			new Ticket(Screenings[1], USERS[1], 247, SeatStatus.TAKEN),
			new Ticket(Screenings[1], USERS[1], 248, SeatStatus.TAKEN),
			new Ticket(Screenings[2], USERS[2], 387, SeatStatus.TAKEN),
			new Ticket(Screenings[1], USERS[2], 388, SeatStatus.RESERVED),
			new Ticket(Screenings[2], USERS[2], 316, SeatStatus.TAKEN),
			new Ticket(Screenings[2], USERS[0], 317, SeatStatus.TAKEN) };

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

	public void addTestDataToDB() {
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
		for (Movie movie : Movies) {
			movieDAO.add(movie);
		}
	}

	private void addTestUsers() {
		for (User user : USERS) {
			userDAO.add(user);
		}
	}

	private void addTestHalls() {
		for (Hall hall : Halls) {
			hallDAO.add(hall);
		}
	}

	private void addTestTickets() {
		for (Ticket ticket : Tickets) {
			ticketDAO.add(ticket);
		}
	}
}
