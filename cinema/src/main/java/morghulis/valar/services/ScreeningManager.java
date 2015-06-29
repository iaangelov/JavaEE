package morghulis.valar.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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

import morghulis.valar.dao.HallDAO;
import morghulis.valar.dao.ScreeningDAO;
import morghulis.valar.model.Screening;

@Stateless
@Path("screening")
public class ScreeningManager {

	@EJB
	private ScreeningDAO screeningDAO;
	
	@EJB 
	private HallDAO hallDAO;
	
	@GET
	@Produces("application/json")
	public Collection<Screening> getAllScreenings() {
		
		return screeningDAO.getAllScreenings();
	}
	
	@GET
	@Path("{screeningId}")
	@Produces("application/json")
	public Screening getScreeningById(@PathParam("screeningId") String screeningId) {
		
		return screeningDAO.findById(Long.parseLong(screeningId));
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
		System.out.println(movieName);
		return screeningDAO.getAllScreeningsByMovieName(movieName);
	}
	
	@DELETE
	@Path("remove")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response deleteScreening(@QueryParam("id") String id) {
		Screening screening = screeningDAO.findById(Long.parseLong(id));
		if(screening != null) {
			screeningDAO.remove(screening);
		}
		return Response.noContent().build();
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addScreening(Screening screening) {
		System.out.println(screening.toString());
		if(screening != null) {			
			Screening toAdd = new Screening();
			toAdd.setHall(hallDAO.findByHallNumber(screening.getHall().getHallNumber()));
			toAdd.getHall().setScreenings(new ArrayList<>(screeningDAO.getAllScreeningsByMovieName(screening.getMovie().getName())));
			System.out.println(screening.getScreeningDate().getTime().toString());
			Calendar cal = Calendar.getInstance();
			//toAdd.setScreeningDate(screeningDate);
			//toAdd.setScreeningDate(screeningDate);
			screeningDAO.add(toAdd);
		}
	}
	
}
