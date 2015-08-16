package panayotov.week1.services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Service;

import panayotov.week1.dao.HallDAO;
import panayotov.week1.models.Hall;

@Stateless
@Service("hallService")
@Path("halls")
public class HallManager {

	@Inject
	private HallDAO hallDAO;
	
	@PostConstruct
	public void setEJBs() throws NamingException{
		InitialContext ic = new InitialContext();
		hallDAO = (HallDAO) ic.lookup("java:comp/env/ejb/HallEJB");
	}
	
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
