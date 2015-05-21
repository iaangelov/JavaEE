package morghulis.valar.services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import morghulis.valar.dao.UserDAO;
import morghulis.valar.model.User;

@Stateless
@Path("user")
public class UserManager {

	@EJB
	private UserDAO userDAO;

	@GET
	@Produces("application/json")
	public Collection<User> getAllUsers() {
		return this.userDAO.getAllUsers();
	}

}
