package panayotov.week1.services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.UserProvider;

import panayotov.week1.dao.MovieDAO;
import panayotov.week1.dao.TicketDAO;
import panayotov.week1.dao.UserDAO;
import panayotov.week1.models.Movie;
import panayotov.week1.models.Ticket;
import panayotov.week1.models.User;
import panayotov.week1.utils.SeatStatus;

@Path("/admin")
@Service("adminService")
@Stateless
public class AdminManager {

	@Inject
	private UserDAO userDAO;

	@Inject
	private MovieDAO movieDAO;

	@Inject
	private TicketDAO ticketDAO;
	
	private UserProvider userProvider;

	@PostConstruct
	public void setEJBs() throws NamingException{
		InitialContext ic = new InitialContext();
		userDAO = (UserDAO) ic.lookup("java:comp/env/ejb/UserEJB");
		movieDAO = (MovieDAO) ic.lookup("java:comp/env/ejb/MovieEJB");
		ticketDAO = (TicketDAO) ic.lookup("java:comp/env/ejb/TicketEJB");
		userProvider = (UserProvider) ic.lookup("java:comp/env/user/Provider");
	}
	
	@GET
	@Path("/users/all")
	@Produces("application/json")
	public Collection<User> getAllUsers(@Context HttpServletRequest request) throws UnsupportedUserAttributeException, PersistenceException {
		return this.userDAO.getAllUsers(userProvider.getUser(request.getUserPrincipal().getName()));
	}

	@GET
	@Path("/movies/all")
	@Produces("application/json")
	public Collection<Movie> getAllMovies() {
		return this.movieDAO.getAllMovies();
	}

	@POST
	@Path("/movies/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addMovie(Movie movie) {
		this.movieDAO.add(movie);
	}

	@PUT
	@Path("/tickets/confirm")
	public Response confirmTicket(@QueryParam("id") Long id) {
		Ticket ticket = ticketDAO.findById(id);
		if (ticket != null) {
			ticket.setStatus(SeatStatus.TAKEN);
			ticketDAO.add(ticket);
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("remove")
	public Response removeUser(@QueryParam("id") Long id) {
		userDAO.removeUserById(id);
		return Response.ok().build();
	}
}
