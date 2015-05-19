package morghulis.valar.dao;

import java.security.MessageDigest;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import morghulis.valar.model.User;

@Singleton
public class UserDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public void addUser(User user) {
		user.setPassword(getHashedPassword(user.getPassword()));
		entityManager.persist(user);
	}

	public boolean validateCredentials(String username, String password) {
		String checkQuery = "SELECT u FROM User u WHERE username=:username AND password=:password";
		TypedQuery<User> query = entityManager.createQuery(checkQuery,
				User.class);
		query.setParameter("username", username);
		query.setParameter("password", password);
		return makeQuery(query) != null;
	}

	public User findByUsername(String username) {
		String checkQuery = "SELECT u FROM User u WHERE username=:username";
		TypedQuery<User> query = entityManager.createQuery(checkQuery,
				User.class);
		query.setParameter("username", username);
		return makeQuery(query);
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
