package panayotov.week1.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import panayotov.week1.models.Ticket;
import panayotov.week1.models.User;
import panayotov.week1.utils.SeatStatus;

@Singleton
public class TicketDAO extends GenericDAOImpl<Ticket> {

	@Inject
	private UserDAO userDao;

	@Resource
	TimerService timerService;

	@PostConstruct
	public void initejb() throws NamingException {
		InitialContext ic = new InitialContext();
		userDao = (UserDAO) ic.lookup("java:comp/env/ejb/UserEJB");
	}

	private Map<String, Object> parameters = new HashMap<>();

	private Queue<Ticket> reserved = new ConcurrentLinkedDeque<Ticket>();

	public Collection<Ticket> getAllTickets() {
		return getListWithNamedQuery(QueryNames.Ticket_GetAllTickets);
	}

	public void deleteTicket(Long id) {
		Ticket t = findById(id);
		if (t != null) {
			remove(t);
		}
	}

	public Collection<Ticket> findTicketsByUsername(String username) {
		parameters.clear();
		parameters.put("username", username);
		Collection<Ticket> tickets = getListWithNamedQuery(QueryNames.Ticket_FindByUser, parameters);
		List<Ticket> result = new ArrayList<Ticket>();
		for (Ticket t : tickets) {
			if (t.getStatus() != SeatStatus.AVAILABLE) {
				result.add(t);
			}
		}
		return result;
	}

	public Collection<Ticket> findTicketsByUserId(User user, long... userId) {
		parameters.clear();
		parameters.put("userId", user.getId());
		Collection<Ticket> tickets = getListWithNamedQuery(QueryNames.Ticket_FindByUserID, parameters);

		List<Ticket> result = new ArrayList<Ticket>();
		for (Ticket t : tickets) {
			if (t.getStatus() != SeatStatus.AVAILABLE) {
				result.add(t);
			}
		}
		return result;
	}

	public Collection<Ticket> findTicketsByScreeningId(long screeningId) {
		parameters.clear();
		parameters.put("screeningId", screeningId);
		return getListWithNamedQuery(QueryNames.Ticket_FindByScreeningID, parameters);
	}

	//
	// public Ticket findTicketByScreeingAndSeatNumber(long screeningId, int
	// seatNum){
	// String textQuery =
	// "select t from Ticket t where t.screening.id =: screeningId and
	// t.seatNumber =: seatNum";
	// TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class);
	// query.setParameter("screeningId", screeningId);
	// query.setParameter("seatNum", seatNum);
	// return queryTicket(query);
	// }
	//
	public void buyTicket(Ticket ticket, User user) {
		// System.out.println(user!=null);
		Ticket ticketToBuy = findById(ticket.getId());
		User userWhoBuysTicket = userDao.findByUsername(user.getUsername());
		// System.out.println(ticket);
		// System.out.println(userWhoBuysTicket);
		try {
			if (ticket.getStatus().equals(SeatStatus.AVAILABLE)) {
				ticketToBuy.setStatus(SeatStatus.TAKEN);
				ticketToBuy.setUser(userWhoBuysTicket);
				userWhoBuysTicket.getTickets().add(ticketToBuy);
			} else if (ticket.getStatus().equals(SeatStatus.RESERVED)) {
				ticketToBuy.setStatus(SeatStatus.TAKEN);
			}
		} catch (NullPointerException ex) {
			ticketToBuy.setStatus(SeatStatus.TAKEN);
			ticketToBuy.setUser(userWhoBuysTicket);
			userWhoBuysTicket.getTickets().add(ticketToBuy);
		}
	}

	public void reserveTicket(Ticket ticket, String user) {
		Ticket ticketToReserve = findById(ticket.getId());
		User userFromDB = userDao.findByUsername(user);
		try {
			if (ticket.getStatus().equals(SeatStatus.AVAILABLE)) {
				ticketToReserve.setStatus(SeatStatus.RESERVED);
				ticketToReserve.setUser(userFromDB);
				//userFromDB.getTickets().add(ticketToReserve);
				this.reserved.add(ticketToReserve);
				add(ticketToReserve);
				timerService.createTimer(600_000, "reserved ticket");
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			ticket.setStatus(SeatStatus.RESERVED);
			ticketToReserve.setUser(userFromDB);
			//userFromDB.getTickets().add(ticketToReserve);
			this.reserved.add(ticketToReserve);
			timerService.createTimer(600_000, "reserved ticket");
		}
	}

	@Timeout
	public void timeout(Timer timer) {
		timer.cancel();
		Ticket returned = reserved.poll();
		if (findById(returned.getId()) != null) {
			cancelReservation(returned.getId());
		}
	}

	public void cancelReservation(long ticketId) {
		Ticket ticket = findById(ticketId);
		if (ticket.getStatus().equals(SeatStatus.RESERVED)) {
			ticket.setStatus(SeatStatus.AVAILABLE);
			User userFromDB = userDao.findByUsername((ticket.getUser().getUsername()));
			userFromDB.getTickets().remove(ticket);
		}
	}

	public Collection<Ticket> cancelMyReservations(User user) {
		List<Ticket> tickets = null;
		String textQuery = "select t from Ticket t where t.status =: statusReserved and t.user.id =: userId";
		TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class);
		query.setParameter("statusReserved", "Reserved");
		query.setParameter("userId", user);
		tickets = query.getResultList();
		for (Ticket currentTicket : tickets) {
			deleteTicket(currentTicket.getId());
		}
		return tickets;
	}

	public int confirmReservation(User user) {
		parameters.clear();
		parameters.put("statusReserved", "Reserved");
		parameters.put("userId", user);
		Collection<Ticket> tickets = getListWithNamedQuery(QueryNames.Ticket_FindReservedByUserID, parameters);

		for (Ticket currentTicket : tickets) {
			if (currentTicket.getStatus().equals(SeatStatus.RESERVED)) {
				currentTicket.setStatus(SeatStatus.TAKEN);
				editTicket(currentTicket);
			}
		}
		if (tickets.size() == 0) {
			return -1;
		}
		return 1;
	}

	public void editTicket(Ticket ticket) {
		em.merge(ticket);
	}

	public Collection<Ticket> findTicketsByNames(String names) {
		// no check if given only 1 name
		String[] theNames = names.split(" ");
		String fname;
		String lname;

		if (theNames.length == 1) {
			fname = theNames[0];
			lname = "";
		} else {
			fname = theNames[0];
			lname = theNames[1];
		}

		parameters.clear();
		parameters.put("fname", fname);
		parameters.put("lname", lname);

		return getListWithNamedQuery(QueryNames.Ticket_FindByUserNames, parameters);
	}

	public Long getMaxID() {
		Query query = em.createQuery("SELECT u.id FROM Ticket u ORDER BY u.id DESC");
		query.setMaxResults(1);
		return (Long) query.getSingleResult();
	}
}
