package morghulis.valar.dao;

import java.security.MessageDigest;
import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import morghulis.valar.model.User;
import morghulis.valar.services.UserContext;
import morghulis.valar.utils.UserType;

@Singleton
public class UserDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private UserContext userContext;

	public List<User> getAllUsers() {
		String textQuery;
		if (userContext.getCurrentUser().getUserType() == UserType.ADMINISTRATOR) {
			textQuery = "SELECT u FROM User u";
		} else {
			textQuery = "SELECT u FROM User u WHERE u.userType = 'Customer'";
		}

		return entityManager.createQuery(textQuery, User.class).getResultList();
	}

	public void addUser(User user) {
		if (findByUsername(user.getUsername()) == null) {
			user.setPassword(getHashedPassword(user.getPassword()));
			entityManager.persist(user);
		}
	}

	public boolean usernameTaken(String username) {
		return findByUsername(username) != null;
	}

	public boolean validateCredentials(String username, String password) {
		String checkQuery = "SELECT u FROM User u WHERE u.username=:username AND u.password=:password";
		TypedQuery<User> query = entityManager.createQuery(checkQuery,
				User.class);
		query.setParameter("username", username);
		query.setParameter("password", getHashedPassword(password));
		return makeQuery(query) != null;
	}

	public User findByUsername(String username) {
		String checkQuery = "SELECT u FROM User u WHERE u.username=:username";
		TypedQuery<User> query = entityManager.createQuery(checkQuery,
				User.class);
		query.setParameter("username", username);
		return makeQuery(query);
	}
	
	public User findById(Long id){
		return entityManager.find(User.class, id);
	}
	
	public void removeUserById(Long id){
		entityManager.remove(findById(id));
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
