package morghulis.valar.services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import morghulis.valar.dao.ScreeningDAO;
import morghulis.valar.model.Screening;

@Stateless
@Path("screening")
public class ScreeningManager {

	@EJB
	private ScreeningDAO screeningDAO;
	
	@GET
	@Produces("application/json")
	public Collection<Screening> getAllScreenings() {
		
		return screeningDAO.getAllScreenings();
	}
	
	@GET
	@Path("{screeningId}")
	@Produces("application/json")
	public Screening getScreeningById(@PathParam("screeningId") String screeningId) {
		
		return screeningDAO.findScreeningById(Long.parseLong(screeningId));
	}
	
	@GET
	@Path("screeningsByHallId")
	@Produces("application/json")
	public Collection<Screening> getScreeningsByHallId(@QueryParam("hallId") String hallId) {
		
		return screeningDAO.getAllScreeningsByHallId(Long.parseLong(hallId));
	}
	
	@GET
	@Path("screeningsByMovieName")
	@Produces("application/json")
	public Collection<Screening> getScreeningsByMovieId(@QueryParam("movieName") String movieName) {
		
		return screeningDAO.getAllScreeningsByMovieName(movieName);
	}
	
	
}
