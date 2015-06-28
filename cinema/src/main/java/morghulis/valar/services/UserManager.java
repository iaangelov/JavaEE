package morghulis.valar.services;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import morghulis.valar.dao.UserDAO;
import morghulis.valar.model.User;
import morghulis.valar.utils.UserType;

@Stateless
@Path("user")
public class UserManager {

	private static final Response RESPONSE_OK = Response.ok().build();

	@EJB
	private UserDAO userDAO;

	@Inject
	private UserContext userContext;

	@GET
	@Path("type")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserType() {
		return userContext.getCurrentUser().getUserType().getText();
	}
	
	@GET
	@Path("names")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrrentUserNames(){
		User current = userContext.getCurrentUser();
		if(current == null){
			return null;
		}
		return current.getFirst_name() + " " + current.getSurname();
	}
	
	@GET
	@Path("authorized")
	public Response isAuthorized(){
		if(userContext.getCurrentUser() == null){
			return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
		}
		return RESPONSE_OK;
	}

	@GET
	@Path("current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUser() {
		if (userContext.getCurrentUser() == null) {
			return null;
		}
		return userContext.getCurrentUser().getUsername();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(User newUser) {
		if (userDAO.usernameTaken(newUser.getUsername())) {
			return Response.status(HttpURLConnection.HTTP_CONFLICT).build();
		}
		newUser.setUserStatus(UserType.CUSTOMER);
		userDAO.add(newUser);
		userContext.setCurrentUser(newUser);
		return RESPONSE_OK;
	}

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUser(User user) {
		boolean isValidUser = userDAO.validateCredentials(user.getUsername(),
				user.getPassword());
		if (!isValidUser) {
			return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
		}

		userContext.setCurrentUser(userDAO.findByUsername(user.getUsername()));
		return RESPONSE_OK;
	}
	
	@GET
	@Path("logout")
	public void logoutUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		request.getSession().invalidate();
		try {
			response.sendRedirect(request.getContextPath() + "/index.html");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
