package panayotov.week1.utils;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import panayotov.week1.dao.UserDAO;
import panayotov.week1.models.Ticket;
import panayotov.week1.models.User;

import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.UserProvider;

/**
 * Servlet Filter implementation class CurrentUserServlet
 */
@WebFilter("/CurrentUserServlet")
public class CurrentUserServlet implements Filter {

	@Inject
	private UserDAO userDAO;

	private UserProvider userProvider;

	private User currentUser;

	public CurrentUserServlet() throws NamingException {

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			contextUser(request);
		} catch (UnsupportedUserAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		InitialContext ic;
		try {
			ic = new InitialContext();
			userDAO = (UserDAO) ic.lookup("java:comp/env/ejb/UserEJB");
			userProvider = (UserProvider) ic
					.lookup("java:comp/env/user/Provider");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private User contextUser(@Context ServletRequest request)
			throws UnsupportedUserAttributeException {
		HttpServletRequest context = (HttpServletRequest) request;
		if (context.getUserPrincipal() != null) {
			if (currentUser != null) {
				User user = userDAO.findByUsername(context.getUserPrincipal()
						.getName());
				if (context.getUserPrincipal() == null || user == null) {
					return getCurrentUser();
				}

				return user;
			}
		}
		return null;
	}

	private User getCurrentUser() throws UnsupportedUserAttributeException {
		User user = null;
		try {
			if (userProvider.getCurrentUser() != null) {
				com.sap.security.um.user.User usera = userProvider
						.getCurrentUser();
				if (userDAO.findByUsername(userProvider.getCurrentUser()
						.getName()) == null) {
					String email = usera.getAttribute("email");
					String fname = usera.getAttribute("firstname");
					String lname = usera.getAttribute("lastname");
					String username = usera.getName();
					UserType type;
					if (usera.hasRole("Admin")) {
						type = UserType.ADMINISTRATOR;
					} else {
						type = UserType.CUSTOMER;
					}
					user = new User(username,
							userDAO.getHashedPassword("password"), email,
							fname, lname, type);
					user.setTickets(new ArrayList<Ticket>());
					currentUser = user;
					userDAO.addNew(user);
				}
			} else {
				user = userDAO.findByUsername(userProvider.getCurrentUser()
						.getName());
			}
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentUser = user;
		return user;
	}
}
