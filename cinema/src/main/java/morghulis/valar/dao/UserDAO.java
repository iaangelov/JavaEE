package morghulis.valar.dao;

import java.security.MessageDigest;
import java.util.Collection;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

import morghulis.valar.model.User;
import morghulis.valar.services.UserContext;
import morghulis.valar.utils.UserType;

@Singleton
public class UserDAO extends GenericDAOImpl<User> {

	@Inject
	private UserContext userContext;

	public Collection<User> getAllUsers() {		
		if (userContext.getCurrentUser().getUserType() == UserType.ADMINISTRATOR) {
			return getAllWiThNamedQuery("getAllAdmin");
		}
		
		return getAllWiThNamedQuery("getAllUser");
	}

	@Override
	public void add(User user) {
		if (findByUsername(user.getUsername()) == null) {
			user.setPassword(getHashedPassword(user.getPassword()));
			em.persist(user);
		}
	}

	public boolean usernameTaken(String username) {
		return findByUsername(username) != null;
	}

	public boolean validateCredentials(String username, String password) {
		String checkQuery = "SELECT u FROM User u WHERE u.username=:username AND u.password=:password";
		TypedQuery<User> query = em.createQuery(checkQuery, User.class);
		query.setParameter("username", username);
		query.setParameter("password", getHashedPassword(password));
		return makeQuery(query) != null;
	}

	public User findByUsername(String username) {
		String checkQuery = "SELECT u FROM User u WHERE u.username=:username";
		TypedQuery<User> query = em.createQuery(checkQuery, User.class);
		query.setParameter("username", username);
		return makeQuery(query);
	}

	public void removeUserById(Long id) {
		em.remove(findById(id));
	}

	private String getHashedPassword(String password) {
		try {
			MessageDigest mda = MessageDigest.getInstance("SHA-512");
			password = new String(mda.digest(password.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}
}
