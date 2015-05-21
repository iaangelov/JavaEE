package morghulis.valar.services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import morghulis.valar.dao.MovieDAO;
import morghulis.valar.model.Movie;

@Stateless
@Path("movie")
public class MovieManager {

	@EJB
	private MovieDAO movieDAO;
	
	@GET
	@Produces("application/json")
	private Collection<Movie> getAllMovies() {
		return movieDAO.getAllMovies();
	}
	
}
