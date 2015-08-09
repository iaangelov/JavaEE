package panayotov.week1.services;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import panayotov.week1.dao.UserDAO;
import panayotov.week1.models.User;
import panayotov.week1.utils.UserType;


@Path("/user")
@Service("userService")
@Stateless
public class UserManager {

	private static final Response RESPONSE_OK = Response.ok().build();

	@EJB
	private UserDAO userDAO;
	
	@Context
	private HttpServletRequest request;
	
	
	@PostConstruct
	public void setEJBs() throws NamingException {
		InitialContext ic = new InitialContext();
		userDAO = (UserDAO) ic.lookup("java:comp/env/ejb/UserEJB");
	}

	@GET
	@Path("type")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserType(@Context SecurityContext context) {
		User user = (User) context.getUserPrincipal();
		if(user == null){
			return null;
		}
		return user.getUserType().getText();
	}

	@GET
	@Path("names")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrrentUserNames(@Context SecurityContext context) {
		User current = null;
		if (null == request.getSession(false)) {
			return null;
		} else {
			current = (User) context.getUserPrincipal();
			return current.getFirst_name() + " " + current.getSurname();
		}
	}

	@GET
	@Path("authorized")
	public Response isAuthorized() {
		if (null == request.getSession(false)) {
			return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
		}
		return RESPONSE_OK;
	}

	@GET
	@Path("current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUser(@Context SecurityContext context) {
		if (request.getSession(false) == null) {
			return "";
		}
		return ((User)context.getUserPrincipal()).getUsername();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(User newUser) {
		System.out.println(newUser.toString());
		if (userDAO.usernameTaken(newUser.getUsername())) {
			return Response.status(HttpURLConnection.HTTP_CONFLICT).build();
		}
		newUser.setUserStatus(UserType.CUSTOMER);
		userDAO.add(newUser);
		request.getSession(true);
		return RESPONSE_OK;
	}

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUser(User user) {
		boolean isValidUser = userDAO.validateCredentials(user.getUsername(), user.getPassword());
		if (!isValidUser) {
			return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
		}
		request.getSession(true);
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
