package panayotov.week1.services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import panayotov.week1.dao.MovieDAO;
import panayotov.week1.models.Movie;

@Path("movie")
@Service("movieService")
@Stateless
public class MovieManager {

	@Inject
	private MovieDAO movieDAO;

	@PostConstruct
	public void setEJBs() throws NamingException{
		InitialContext ic = new InitialContext();
		movieDAO = (MovieDAO) ic.lookup("java:comp/env/ejb/MovieEJB");
	}
	
	@GET
	@Produces("application/json")
	public Collection<Movie> getAllMovies() {
		return movieDAO.getAllMovies();
	}

	@GET
	@Path("byId/{movieId}")
	@Produces("application/json")
	public Movie getMovieById(@PathParam("movieId") String movieId) {
		return movieDAO.findById(Long.parseLong(movieId));
	}

	@GET
	@Path("{movieName}")
	@Produces("application/json")
	public Movie getMovieByName(@PathParam("movieName") String movieName) {
		return movieDAO.findByName(movieName);
	}

	@DELETE
	@Path("remove")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response deleteMovie(@QueryParam("movieId") String movieId) {
		Movie movieToRemove = movieDAO.findById(Long.parseLong(movieId));
		if (movieToRemove != null) {
			movieDAO.remove(movieToRemove);
		}
		return Response.noContent().build();
	}

	//@PUT
	@POST
	@Path("add")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void addMovie(Movie newMovie) {
		if (newMovie != null) {
			movieDAO.add(newMovie);
		}
	}
	
	@GET
	@Path("max")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMaxID(){
		return String.valueOf(movieDAO.getMaxID());
		
	}
}
