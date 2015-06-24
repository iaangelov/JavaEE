package morghulis.valar.dao;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import morghulis.valar.model.Hall;
import morghulis.valar.model.Movie;
import morghulis.valar.model.Screening;
import morghulis.valar.model.Ticket;
import morghulis.valar.model.User;
import morghulis.valar.services.UserContext;
import morghulis.valar.utils.SeatStatus;
import morghulis.valar.utils.UserType;

@Singleton
@LocalBean
public class TicketDAO {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private UserContext userContext;

	@Inject
	private UserDAO userDao;
	
	@Resource
    TimerService timerService;

	private Queue<Ticket> reserved = new ArrayDeque<Ticket>();
	
	public List<Ticket> getAllTickets() {
		String query = "SELECT t FROM Ticket t";

		return em.createQuery(query, Ticket.class).getResultList();
	}

	public void addTicket(Ticket ticket) {
		em.persist(ticket);
	}

	public void deleteTicket(long id) {
		Ticket t = em.find(Ticket.class, id);
		if (t != null) {
			em.remove(t);
		}
	}

	public Ticket findById(long key) {
		return em.find(Ticket.class, key);
	}

	public Collection<Ticket> findTicketsByUserId(long... userId) {
		List<Ticket> tickets = null;
		String textQuery = "select t from Ticket t where t.user.id =: userId";
		if (userContext.getCurrentUser().getUserType()
				.equals(UserType.ADMINISTRATOR)
				&& userId.length != 0) {
			TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class)
					.setParameter("userId", userId[0]);
			tickets = query.getResultList();
		}

		else {
			TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class)
					.setParameter("userId",
							userContext.getCurrentUser().getId());
			tickets = query.getResultList();
		}
		List<Ticket> result = new ArrayList<Ticket>();
		for(Ticket t : tickets){
			if(t.getStatus() != SeatStatus.AVAILABLE){
				result.add(t);
			}
		}
		return result;
	}

	public Collection<Ticket> findTicketsByScreeningId(long screeningId) {
		String textQuery = "select t from Ticket t where t.screening.id =: screeningId";
		TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class)
				.setParameter("screeningId", screeningId);
		List<Ticket> tickets = query.getResultList();
		return tickets;
	}

	//
	// public Ticket findTicketByScreeingAndSeatNumber(long screeningId, int
	// seatNum){
	// String textQuery =
	// "select t from Ticket t where t.screening.id =: screeningId and t.seatNumber =: seatNum";
	// TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class);
	// query.setParameter("screeningId", screeningId);
	// query.setParameter("seatNum", seatNum);
	// return queryTicket(query);
	// }
	//
	public void buyTicket(Ticket ticket, User user) {
		//System.out.println(user!=null);
		Ticket ticketToBuy = findById(ticket.getId());
		User userWhoBuysTicket = userDao.findByUsername(user.getUsername());
		//System.out.println(ticket);
		//System.out.println(userWhoBuysTicket);
		try{
			if (ticket.getStatus().equals(SeatStatus.AVAILABLE)) {
				ticketToBuy.setStatus(SeatStatus.TAKEN);
				ticketToBuy.setUser(userWhoBuysTicket);
				userWhoBuysTicket.getTickets().add(ticketToBuy);
			}
			else if (ticket.getStatus().equals(SeatStatus.RESERVED)) {
				ticketToBuy.setStatus(SeatStatus.TAKEN);
			}
		}catch (NullPointerException ex){
			ticketToBuy.setStatus(SeatStatus.TAKEN);
			ticketToBuy.setUser(userWhoBuysTicket);
			userWhoBuysTicket.getTickets().add(ticketToBuy);
		}
	}

	public void reserveTicket(Ticket ticket, User user) {
		Ticket ticketToReserve = findById(ticket.getId());
		User userFromDB = userDao.findById(user.getId());
		try {
		if (ticket.getStatus().equals(SeatStatus.AVAILABLE)) {
			ticketToReserve.setStatus(SeatStatus.RESERVED);
			ticketToReserve.setUser(user);
			userFromDB.getTickets().add(ticketToReserve);
			this.reserved.add(ticketToReserve);
			timerService.createTimer(600_000, "reserved ticket");
			}
		}
		catch(NullPointerException ex){
			ticket.setStatus(SeatStatus.RESERVED);
			ticketToReserve.setUser(user);
			userFromDB.getTickets().add(ticketToReserve);
			this.reserved.add(ticketToReserve);
			timerService.createTimer(600_000, "reserved ticket");
		}
	}
	
	@Timeout
	public void timeout(Timer timer) {
		timer.cancel();
		Ticket returned = reserved.poll();
		cancelReservation(returned.getId());
	}

	public void cancelReservation(long ticketId) {
		Ticket ticket = findById(ticketId);
		if (ticket.getStatus().equals(SeatStatus.RESERVED)) {
			ticket.setStatus(SeatStatus.AVAILABLE);
			User userFromDB = userDao.findByUsername((ticket.getUser()
					.getUsername()));
			userFromDB.getTickets().remove(ticket);
		}
	}
	
	public void confirmReservation() {
		
		List<Ticket> tickets = null;
		String textQuery = "select t from Ticket t where t.status =: statusReserved and t.user.id =: userId";
		TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class);
		query.setParameter("statusReserved", "Reserved");
		query.setParameter("userId", userContext.getCurrentUser().getId());
		tickets = query.getResultList();
		System.out.println(tickets.size());
		for(Ticket currentTicket : tickets){
			if (currentTicket.getStatus().equals(SeatStatus.RESERVED)) {
				currentTicket.setStatus(SeatStatus.TAKEN);
				editTicket(currentTicket);
			}
		}
	}

	public void createNewTicket(Ticket ticket) {
		em.persist(ticket);
	}

	public void editTicket(Ticket ticket) {
		em.merge(ticket);
	}

	private Ticket queryTicket(TypedQuery<Ticket> query) {
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	private static Movie[] Movies= {
    	new Movie("Get Hard"),
        new Movie("Frozen"),
        new Movie("Cinderella"),
        new Movie("The Wedding Ringer"),
        new Movie("Beauty and the Beast"),
        new Movie("Tangled")};
    
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
    	new Screening(Halls[0], Movies[0], Calendar.getInstance()),
    	new Screening(Halls[1], Movies[1], Calendar.getInstance()),
    	new Screening(Halls[2], Movies[1], Calendar.getInstance()),
    	new Screening(Halls[2], Movies[1], Calendar.getInstance()),
    	new Screening(Halls[2], Movies[1], Calendar.getInstance()),
    	new Screening(Halls[2], Movies[1], Calendar.getInstance()),
    	new Screening(Halls[2], Movies[1], Calendar.getInstance())
    };
    
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
        new Ticket(Screenings[2], USERS[0], 317,SeatStatus.TAKEN)
    };    
    
	public static void main(String[] args) {
		System.out.println(Tickets[1]);
		System.out.println(SeatStatus.RESERVED.toString());
	}
}
