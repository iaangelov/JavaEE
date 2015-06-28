package morghulis.valar.services;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import morghulis.valar.dao.ReservationDAO;
import morghulis.valar.dao.ScreeningDAO;
import morghulis.valar.dao.TicketDAO;
import morghulis.valar.model.Reservation;
import morghulis.valar.model.Ticket;

@Stateless
@Path("reservation")
public class ReservationManager {

	@EJB
	private TicketDAO ticketDao;

	@EJB
	private ScreeningDAO screeningDao;
	
	@EJB
	private ReservationDAO reservationDAO;

	@Inject
	private UserContext UserContext;

	@GET
	@Path("all")
	@Produces("application/json")
	public Collection<Reservation> getAllReservations() {
		return reservationDAO.getAllReservations();
	}

	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addReservation(Reservation newReservation) {
		if (newReservation != null) {
			reservationDAO.add(newReservation);
		}
	}
	
	@DELETE
	@Path("remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteReservation(@QueryParam("reservationId") String reservationId) {
		Reservation reservationToRemove = reservationDAO.findById(Long.parseLong(reservationId));
		if (reservationToRemove != null) {
			reservationDAO.deleteReservation(reservationToRemove.getId());
		}
		return Response.noContent().build();
	}
	
	@GET
	@Path("/byUserId/{userId}")
	@Produces("application/json")
	public Collection<Reservation> getReservationsByUserId(@PathParam("userId") Long id) {
		Collection<Reservation> findReservationsByUserId = null;
		try {
			findReservationsByUserId = reservationDAO.findReservationsByUserId(id);
		} catch (NullPointerException ex) {
			
		}
		return findReservationsByUserId.size() > 0 ? findReservationsByUserId : new ArrayList<Reservation>();
	}
	
	@GET
	@Path("/myreservations")
	@Produces("application/json")
	public Collection<Reservation> getMyReservations() {
		Collection<Reservation> findReservationsByUserId = reservationDAO.findReservationsByUserId();
		return findReservationsByUserId.size() > 0 ? findReservationsByUserId : null;
	}
	
	@GET
	@Path("/byUserNames/{names}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getReservationssByNames(@PathParam("names") String names) {
		return reservationDAO.findReservationsByNames(names);
	}
	
	@POST
	@Path("/confirmSingleReservation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmSingleReservation(@PathParam("userId") Long id1, @PathParam("reservationId") Long id2) {
		reservationDAO.confirmSingleReservation(id1, id2);
		
		return Response.noContent().build();
	}

	@DELETE
	@Path("/cancelSingleReservation")
	public Response cancelSingleReservation(@PathParam("userId") Long id1, @PathParam("reservationId") Long id2) {
		reservationDAO.cancelSingleReservation(id1, id2);
		return Response.noContent().build();
	}
	
}
