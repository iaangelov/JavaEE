package panayotov.week1.services;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.sap.security.auth.login.LoginContextFactory;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.UserProvider;

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

	private UserProvider userProvider;

	@PostConstruct
	public void setEJBs() throws NamingException {
		InitialContext ic = new InitialContext();
		userDAO = (UserDAO) ic.lookup("java:comp/env/ejb/UserEJB");
		this.userProvider = (UserProvider) ic.lookup("java:comp/env/user/Provider");
	}

	@PermitAll
	@GET
	@Path("type")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserType(@Context HttpServletRequest req) throws PersistenceException {
		if(userProvider.getUser(req.getUserPrincipal().getName()).hasRole("Admin")){
			return UserType.ADMINISTRATOR.getText();
		}
		return UserType.CUSTOMER.getText();
	}

	@PermitAll
	@GET
	@Path("names")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrrentUserNames(@Context HttpServletRequest req) {
		if (req.getUserPrincipal() != null) {
			try {
				com.sap.security.um.user.User user = userProvider.getUser(req.getUserPrincipal().getName());
				return user.getAttribute("firstname") + " " + user.getAttribute("lastname");
			} catch (PersistenceException | UnsupportedUserAttributeException e) {
	
				e.printStackTrace();
			}
		}
		return null;
	}

	@PermitAll
	@GET
	@Path("authorized")
	public Response isAuthorized(@Context HttpServletRequest req) {
		if (req.getUserPrincipal() != null) { 
			try { 
				com.sap.security.um.user.User user = userProvider.getUser(req.getUserPrincipal().getName());
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
			return RESPONSE_OK;
		}
		return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();

	}

	@PermitAll
	@GET
	@Path("current")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUser(@Context HttpServletRequest req) {
		if (req.getUserPrincipal() != null) {
			try { 
				com.sap.security.um.user.User user = userProvider.getUser(req.getUserPrincipal().getName());
				return user.getName();
			} catch (PersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	@PermitAll
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(@Context HttpServletRequest req, @Context HttpServletResponse response, User newUser) throws IOException {
		response.sendRedirect("https://www.sdn.sap.com/irj/scn/register");
		return RESPONSE_OK;
	}

	@Path("login")
	public Response loginUser(@Context HttpServletResponse response, @Context HttpServletRequest req)
			throws IOException {
		response.sendRedirect(req.getContextPath() + "/index.html");
		return RESPONSE_OK;
	}

	@PermitAll
	@GET
	@Path("logout")
	public void logoutUser(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		LoginContext loginContext = null;
		if (request.getRemoteUser() != null) {
			try {
				loginContext = LoginContextFactory.createLoginContext();
				loginContext.logout();
			} catch (LoginException e) {
				try {
					response.getWriter().println("Logout failed. Reason: " + e.getMessage());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				response.getWriter().println("You have successfully logged out.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
