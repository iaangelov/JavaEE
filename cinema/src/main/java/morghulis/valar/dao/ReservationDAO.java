package morghulis.valar.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;

import morghulis.valar.model.Reservation;
import morghulis.valar.model.Ticket;
import morghulis.valar.services.UserContext;
import morghulis.valar.utils.SeatStatus;

@Singleton
public class ReservationDAO extends GenericDAOImpl<Reservation>{
	@Inject
	private UserContext userContext;

	@Inject
	private UserDAO userDao;

	@Inject
	private TicketDAO ticketDao;
	
	private Map<String, Object> parameters = new HashMap<>();

	public Collection<Reservation> getAllReservations() {
		return getListWithNamedQuery(QueryNames.Reservation_GetAllReservations);
	}
	
	public void deleteReservation(Long id) {
		Reservation r = findById(id);
		if (r != null) {
			remove(r);
		}
	}
	
	public Collection<Reservation> findReservationsByUserId(long... userId) {
		Collection<Ticket> ticketsFromUser = ticketDao.findTicketsByUserId(userId);
		Collection<Reservation> set = new HashSet<Reservation>();
		for(Ticket t : ticketsFromUser){
			set.add(t.getReservation());
		}
		
		return set;
	}
	
	public Collection<Reservation> findReservationsByNames(String names) {
		Collection<Ticket> ticketsFromUser = ticketDao.findTicketsByNames(names);
		Collection<Reservation> set = new HashSet<Reservation>();
		for(Ticket t : ticketsFromUser){
			set.add(t.getReservation());
		}
		
		return set;
	}
	
	// TVA SA TI METODITE ZA KOP4ETATA CONFIRM I CANCEL V PROFILE PANEL/ KASIERSKIQ PANEL
	//MOJE DA MAHAME REZERVACIQTA KOGATO VSI4KI MESTA V NEQ SA CONFIRMNATI?? NE SUM GO PISAL OSTE
	public void confirmSingleReservation(long userId, long reservationId) {
		
		Collection<Ticket> findTicketsByUserId = ticketDao.findTicketsByUserId(userId);
		if(findTicketsByUserId.size() != 0){
			for (Ticket t : findTicketsByUserId) {
				if(t.getReservation().getId() == reservationId){
					if (t.getStatus().equals(SeatStatus.RESERVED)) {
						t.setStatus(SeatStatus.TAKEN);
						ticketDao.editTicket(t);
					}
				}
			}
		}
	}
	
	public void cancelSingleReservation(long userId, long reservationId){
		Collection<Ticket> findTicketsByUserId = ticketDao.findTicketsByUserId(userId);
		for (Ticket t : findTicketsByUserId) {
			if(t.getReservation().getId() == reservationId){
				if (t.getStatus().equals(SeatStatus.RESERVED)) {
					Reservation reservationFromDB = findById(reservationId);
					reservationFromDB.getTickets().remove(t);
					ticketDao.cancelReservation(t.getId());
				}
			}
		}
	}
		
	
}
