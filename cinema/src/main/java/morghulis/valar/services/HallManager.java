package morghulis.valar.services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import morghulis.valar.dao.HallDAO;
import morghulis.valar.model.Hall;

@Stateless
@Path("halls")
public class HallManager {

	@EJB
	private HallDAO hallDAO;
	
	@GET
	@Produces("application/json")
	public Collection<Hall> getAllHalls() {
		return hallDAO.getAllHalls();
	}
	
//	@GET
//	@Path("seats/{hallId}")
//    @Produces("application/json")
//    public List<Ticket> getAllSeatsinHall(@PathParam("hallId")Long id) {
//		hallDAO.findHallById(id).get
//        return hallDAO.findHallById(id)
//    }
}
