package panayotov.week1.services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import panayotov.week1.dao.ScreeningDAO;
import panayotov.week1.dao.TicketDAO;
import panayotov.week1.dao.UserDAO;
import panayotov.week1.models.Screening;
import panayotov.week1.models.Ticket;
import panayotov.week1.models.User;
import panayotov.week1.utils.SeatStatus;
import panayotov.week1.utils.UserType;

import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.UserProvider;

@Stateless
@Service("ticketService")
@Path("ticket")
public class TicketManager {

	@Inject
	private TicketDAO ticketDao;

	@Inject
	private ScreeningDAO screeningDao;

	@Inject
	private UserDAO userDAO;

	private UserProvider userProvider;

	private User currentUser;

	@PostConstruct
	public void setEJBs() throws NamingException {
		InitialContext ic = new InitialContext();
		screeningDao = (ScreeningDAO) ic
				.lookup("java:comp/env/ejb/ScreeningEJB");
		ticketDao = (TicketDAO) ic.lookup("java:comp/env/ejb/TicketEJB");
		userDAO = (UserDAO) ic.lookup("java:comp/env/ejb/UserEJB");
		userProvider = (UserProvider) ic.lookup("java:comp/env/user/Provider");
	}

	@GET
	@Path("roles")
	public String getRoles() throws UnsupportedUserAttributeException,
			PersistenceException {
		getCurrentUser();
		return Arrays.toString(userProvider.getCurrentUser().getRoles()
				.toArray());
	}

	private User contextUser(@Context SecurityContext context)
			throws UnsupportedUserAttributeException {
		User user = userDAO.findByUsername(context.getUserPrincipal().getName());
		if (context.getUserPrincipal() == null || user == null) {
			return getCurrentUser();
		}
		
		return user;
	}

	private User getCurrentUser() throws UnsupportedUserAttributeException {
		User user = null;
		try {
			if (userProvider.getCurrentUser() != null) {
				com.sap.security.um.user.User usera = userProvider
						.getCurrentUser();
				if (userDAO.findByUsername(userProvider.getCurrentUser()
						.getName()) == null) {
					String email = usera.getAttribute("email");
					String fname = usera.getAttribute("firstname");
					String lname = usera.getAttribute("lastname");
					String username = usera.getName();
					UserType type;
					if (usera.hasRole("Admin")) {
						type = UserType.ADMINISTRATOR;
					} else {
						type = UserType.CUSTOMER;
					}
					user = new User(username,
							userDAO.getHashedPassword("password"), email,
							fname, lname, type);
					user.setTickets(new ArrayList<Ticket>());
					currentUser = user;
					userDAO.addNew(user);
				}
			} else {
				user = userDAO.findByUsername(userProvider.getCurrentUser()
						.getName());
			}
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentUser = user;
		return user;
	}

	@GET
	@Path("all")
	@Produces("application/json")
	public Collection<Ticket> getAllTickets() {
		return ticketDao.getAllTickets();
	}

	@POST
	@Path("add")
	public void addTicket(Ticket newTicket) {
		if (newTicket != null) {
			ticketDao.add(newTicket);
		}
	}

	@POST
	@Path("addAndBuy")
	public Response addAndBuy(@Context SecurityContext sc,
			Ticket newTicket) throws PersistenceException,
			UnsupportedUserAttributeException {
		if (newTicket != null) {
			newTicket.setScreening(screeningDao.findById(newTicket
					.getScreening().getId()));
			User current = contextUser(sc);
			newTicket.setUser(current);
			ticketDao.addNew(newTicket);
			ticketDao.buyTicket(newTicket, currentUser);
			return Response.ok().build();
		}
		return Response.noContent().build();

	}

	@POST
	@Path("addAndReserve")
	public Response addAndReserve(@Context SecurityContext sc, Ticket newTicket)
			throws PersistenceException, UnsupportedUserAttributeException {
		if (newTicket != null) {
			User user = userDAO.findByUsername(contextUser(sc).getUsername());
			newTicket.setScreening(screeningDao.findById(newTicket
					.getScreening().getId()));
			newTicket.setUser(user);
			ticketDao.addNew(newTicket);
			ticketDao.reserveTicket(newTicket, contextUser(sc).getUsername());
			return Response.ok().build();
		}
		return Response.noContent().build();

	}

	@DELETE
	@Path("remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTicket(@QueryParam("ticketId") String ticketId) {
		Ticket ticketToRemove = ticketDao.findById(Long.parseLong(ticketId));
		if (ticketToRemove != null) {
			ticketDao.deleteTicket(ticketToRemove.getId());
		}
		return Response.noContent().build();
	}

	// poprincip tozi metod e za administratorite , no ako vse pak customer go
	// izvika s kakuvto i da e parametur 6te my vurne mytickets
	@GET
	@Path("/byUserId/{userId}")
	@Produces("application/json")
	public Collection<Ticket> getTicketsByUserId(@Context SecurityContext sc, @PathParam("userId") Long id)
			throws PersistenceException, UnsupportedUserAttributeException {
		try {
			Collection<Ticket> findTicketsByUserId = ticketDao
					.findTicketsByUserId(contextUser(sc));
		} catch (NullPointerException ex) {
			// throw new NullPointerException();
		}
		Collection<Ticket> result = new ArrayList<Ticket>();
		result.add(new Ticket(new Screening(), new User(), 100,
				SeatStatus.AVAILABLE));
		return result;
	}

	@PUT
	@Path("confirm")
	public Response confirmTicket(@QueryParam("id") Long id) {
		Ticket ticket = ticketDao.findById(id);
		if (ticket != null) {
			ticket.setStatus(SeatStatus.TAKEN);
		}
		return Response.ok().build();
	}

	@GET
	@Path("/byUserNames/{names}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Ticket> getTicketsByNames(@PathParam("names") String names) {
		Collection<Ticket> tickets = ticketDao.findTicketsByNames(names);
		List<Ticket> res = new ArrayList<Ticket>(tickets);
		Collections.sort(res, new Comparator<Ticket>() {
			public int compare(Ticket c1, Ticket c2) {
				return c2.getScreening().getScreeningDate()
						.compareTo(c2.getScreening().getScreeningDate());
			}
		});
		return res;
	}

	// tova e za customeri da si vidqt tehnite bileti..
	// adminite susto mogat da hodqt na kino i da si kupuvat bileti taka 4e i te
	// mogat da si vidqt tehnite s tozi metod
	@GET
	@Path("/mytickets")
	@Produces("application/json")
	public Collection<Ticket> getMyTickets(@Context SecurityContext sc)
			throws PersistenceException, UnsupportedUserAttributeException {
		// try{
		Collection<Ticket> findTicketsByUserId = ticketDao
				.findTicketsByUsername(contextUser(sc).getUsername());
		// } catch(NullPointerException ex){
		// throw new NullPointerException();
		// }
		return findTicketsByUserId.size() > 0 ? findTicketsByUserId : null;
	}

	@GET
	@Path("/byScreeningId/{screeningId}")
	@Produces("application/json")
	public Collection<Ticket> getTicketsByScreeningId(
			@PathParam("screeningId") Long id) {
		return ticketDao.findTicketsByScreeningId(id);
	}

	@PUT
	@Path("/buyTicket")
	public Response buyTicket(@Context SecurityContext sc,
			@QueryParam("ticketId") String ticketId)
			throws PersistenceException, UnsupportedUserAttributeException {
		Ticket ticketToBuy = ticketDao.findById(Long.parseLong(ticketId));
		if (ticketToBuy != null) {
			if (sc.getUserPrincipal() == null) {
				return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED)
						.build();
			}
			User user = contextUser(sc);
			ticketDao.buyTicket(ticketToBuy, getCurrentUser());
		}
		return Response.noContent().build();
	}

	@PUT
	@Path("/reserveTicket")
	public Response reserveTicket(@Context SecurityContext sc,
			@QueryParam("ticketId") String ticketId)
			throws PersistenceException, UnsupportedUserAttributeException {
		Ticket ticketToReserve = ticketDao.findById(Long.parseLong(ticketId));
		if (ticketToReserve != null) {
			ticketDao.reserveTicket(ticketToReserve, sc.getUserPrincipal().getName());
			return Response.ok().build();
		}
		return Response.noContent().build();
	}

	@POST
	@Path("/confirmReservation")
	public Response confirmReservation() throws PersistenceException,
			UnsupportedUserAttributeException {
		int confirmReservation = ticketDao.confirmReservation(getCurrentUser());
		if (confirmReservation == -1) {
			return Response.status(304).build();
		}
		return Response.status(200).build();
	}

	@DELETE
	@Produces("application/json")
	@Path("/cancelMyReservations")
	public Collection<Ticket> cancelMyReservations(@Context SecurityContext sc)
			throws PersistenceException, UnsupportedUserAttributeException {
		Collection<Ticket> cancelMyReservations = ticketDao
				.cancelMyReservations(contextUser(sc));
		return cancelMyReservations;
	}

	@GET
	@Path("/test/{ticketId}")
	@Produces("application/json")
	public Ticket getById(@PathParam("ticketId") Long id) {
		// Ticket findById = null;

		// findById = ticketDao.findById(id);
		// return findById;
		return (Ticket) ticketDao.getAllTickets().toArray()[0];
	}

	@GET
	@Path("max")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMaxID() {
		return String.valueOf(ticketDao.getMaxID());
	}

}