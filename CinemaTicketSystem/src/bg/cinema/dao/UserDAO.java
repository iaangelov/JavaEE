package bg.cinema.dao;

import java.security.MessageDigest;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import bg.cinema.models.User;

@Singleton
public class UserDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public void addUser(User user) {
		user.setPassword(getHashedPassword(user.getPassword()));
		entityManager.persist(user);
	}

	public boolean validateUser(String username, String password) {
		String query = "SELECT user FROM User WHERE user.username=:username AND user.password=:password";
		TypedQuery<User> typedQuery = entityManager.createQuery(query,
				User.class);
		typedQuery.setParameter("username", username);
		typedQuery.setParameter("password", password);
		return makeQuery(typedQuery) != null;
	}

	public User findUserByUsername(String username) {
		String query = "SELECT user FROM User WHERE user.username=:username";
		TypedQuery<User> typedQuery = entityManager.createQuery(query,
				User.class);
		typedQuery.setParameter("username", username);
		return makeQuery(typedQuery);
	}

	private User makeQuery(TypedQuery<User> query) {
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
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
