package slbedu.library.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import slbedu.library.dao.HallDAO;
import slbedu.library.model.Hall;
import slbedu.library.model.Ticket;

@Stateless
@Path("halls")
public class HallManager {

	@EJB
	private HallDAO hallDAO;
	
	@GET
	@Produces("application/json")
	public List<Hall> getAllHalls() {
		for (Hall hall : hallDAO.getAllHalls()){
		    System.out.println(hall.getSeats().size());
		}
		return hallDAO.getAllHalls();
	}
	
	@GET
	@Path("seats/{hallId}")
    @Produces("application/json")
    public List<Ticket> getAllSeatsinHall(@PathParam("hallId")Long id) {
        return hallDAO.findHallById(id).getSeats();
    }
//	@GET
//	@Path("byScreenId/{screenId}")
//    @Produces("application/json")
//    public List<Hall> getHallsByScreeningId(@PathParam("screenId") Long id){
//        return hallDAO.findHallsByScreeningId(id);
//	}
}
