package slbedu.library.services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import slbedu.library.dao.MovieDAO;
import slbedu.library.model.Movie;


@Stateless
@Path("movie")
public class MovieManager {

	@EJB
	private MovieDAO movieDAO;

	@GET
	@Produces("application/json")
	public Collection<Movie> getAllMovies() {
		return movieDAO.getAllMovies();
	}

	/*@GET
	@Produces("application/json")
	public Movie getMovieById(@QueryParam("movieId") String movieId) {
		return movieDAO.findById(Long.parseLong(movieId));
	}*/

	@GET
	@Path("{movieName}")
	@Produces("application/json")
	public Movie getMovieByName(@PathParam("movieName") String movieName) {
		return movieDAO.findByName(movieName);
	}

	@DELETE
	@Path("remove")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMovie(@QueryParam("movieId") String movieId) {
		Movie movieToRemove = movieDAO.findById(Long.parseLong(movieId));
		if (movieToRemove != null) {
			movieDAO.deleteMovie(movieToRemove);
		}
		return Response.noContent().build();
	}

	@PUT
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addMovie(Movie newMovie) {
		if (newMovie != null) {
			movieDAO.addMovie(newMovie);
		}
	}
}
