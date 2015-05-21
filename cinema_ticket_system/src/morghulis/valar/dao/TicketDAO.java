package morghulis.valar.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import morghulis.valar.model.Ticket;
import morghulis.valar.model.User;
import morghulis.valar.utils.SeatStatus;

@Singleton
public class TicketDAO {

	@PersistenceContext
	private EntityManager em;
	
//	@Inject
//	private UserContext userContext;	
	
	public void addTicket(Ticket ticket) {
		Ticket t = em.find(Ticket.class, ticket.getId());
		if (t == null) {
			em.persist(ticket);
		}
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
	
	public List<Ticket> findTicketsByUserId(long userId){
 		String textQuery = "select t from Ticket t where t.user.id =: userId";
 		TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class).setParameter("userId", userId);
 		List<Ticket> tickets = query.getResultList();
 		return tickets;
	}
	
	public List<Ticket> findTicketsByScreeningId(long screeningId){
 		String textQuery = "select t from Ticket t where t.screening.id =: screeningId";
 		TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class).setParameter("screeningId", screeningId);
 		List<Ticket> tickets = query.getResultList();
 		return tickets;
	}
	
	public void buyTicket(Ticket ticket, User userWhoBuysTicket){
		Ticket ticketToBuy = findById(ticket.getId());
		ticketToBuy.setStatus(new SeatStatus(SeatStatus.TAKEN));
		userWhoBuysTicket.getTickets().add(ticket);
	}
	
	public void reserveTicket(Ticket ticket, User user){
		
		
	}
	
	public void cancelReserveTicket(Ticket ticket, User user){
		
	}
	public void createNewTicket(Ticket ticket) {
		em.persist(ticket);
	}
	
	public void editTicket(Ticket ticket){
		em.merge(ticket);
	}
	
	
	
	private Ticket queryTicket(TypedQuery<Ticket> query) {
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
