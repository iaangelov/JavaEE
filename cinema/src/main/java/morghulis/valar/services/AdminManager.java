package morghulis.valar.services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import morghulis.valar.dao.MovieDAO;
import morghulis.valar.dao.UserDAO;
import morghulis.valar.model.Movie;
import morghulis.valar.model.User;

@Stateless
@Path("admin")
public class AdminManager {

	@EJB
	private UserDAO userDAO;
	
	@EJB
	private MovieDAO movieDAO;
	
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

	@DELETE
	@Path("remove") 
	public Response removeUser(@QueryParam("id") Long id) {
		userDAO.removeUserById(id);
		return Response.ok().build();
	}
}
