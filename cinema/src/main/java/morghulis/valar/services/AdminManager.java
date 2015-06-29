package morghulis.valar.services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import morghulis.valar.dao.MovieDAO;
import morghulis.valar.dao.TicketDAO;
import morghulis.valar.dao.UserDAO;
import morghulis.valar.model.Movie;
import morghulis.valar.model.Ticket;
import morghulis.valar.model.User;
import morghulis.valar.utils.SeatStatus;

@Stateless
@Path("admin")
public class AdminManager {

	@EJB
	private UserDAO userDAO;
	
	@EJB
	private MovieDAO movieDAO;
	
	@EJB
	private TicketDAO ticketDAO;
	
	@GET
	@Path("/users/all")
	@Produces("application/json")
	public Collection<User> getAllUsers() {
		return this.userDAO.getAllUsers();
	}
	
	@GET
	@Path("/movies/all")
	@Produces("application/json")
	public Collection<Movie> getAllMovies(){
		return this.movieDAO.getAllMovies();
	}
	
	@POST
	@Path("/movies/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addMovie(Movie movie){
		this.movieDAO.add(movie);
	}
	
	@PUT
	@Path("/tickets/confirm")
	public Response confirmTicket(@QueryParam("id") Long id){
		Ticket ticket = ticketDAO.findById(id);
		if(ticket != null){
			ticket.setStatus(SeatStatus.TAKEN);
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
