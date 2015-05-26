package slbedu.library.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import slbedu.library.model.Hall;
import slbedu.library.model.Screening;
import slbedu.library.model.Ticket;
import slbedu.library.utils.SeatStatus;

@Singleton
public class HallDAO {

	@PersistenceContext
	private EntityManager em;
	
	@EJB
	TicketDAO ticketDAO;

	@EJB
	ScreeningDAO screeningDAO;
	
	public void add(Hall hall) {
		
		em.persist(hall);
	}
	
	
//	public void addSeatsInHall(int numberOfSeatsToAdd, Hall hall){
//	    List<Ticket> seats = hall.getSeats();
//	    Screening screeningForHall = null;
//	    for(Screening s : screeningDAO.getAllScreenings()){
//	        if(s.getHall().getHallNumber() == hall.getHallNumber()){
//	            screeningForHall = s;
//	        }
//	    }
//	    
//	    for(int i = 0; i < numberOfSeatsToAdd; i++){
//	       Ticket newTicket =  new Ticket(screeningForHall, null, i, new SeatStatus(SeatStatus.AVAILABLE));
//	       ticketDAO.addTicket(newTicket);
//	       seats.add(newTicket);
//	    }    
//	}
	
	public void addSeatInHall(List<Ticket> seat, Hall hall){
        List<Ticket> seats = hall.getSeats();
            seats.addAll(seat);      
	}
	
//	 public List<Hall> findHallsByScreeningId(long screeningId){
//       
// }
	
	public Hall findHallById(Long id){
	    return em.find(Hall.class, id);
	}
	
	public List<Hall> getAllHalls() {
		
		return em.createQuery("SELECT h FROM Hall h", Hall.class).getResultList();
	}
	
}
