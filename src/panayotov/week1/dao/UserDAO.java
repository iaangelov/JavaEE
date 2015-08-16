package panayotov.week1.dao;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.persistence.Query;

import panayotov.week1.models.User;

@Singleton
public class UserDAO extends GenericDAOImpl<User> {

	private Map<String, String> parameters = new HashMap<>();

	public Collection<User> getAllUsers(com.sap.security.um.user.User user) {
		if (user.hasRole("Admin")) {
			return getListWithNamedQuery(QueryNames.User_GetAllAsAdmin);
		}

		return getListWithNamedQuery(QueryNames.User_GetAllAsUser);
	}

	@Override
	public void add(User user) {
		if (findByUsername(user.getUsername()) == null) {
			user.setPassword(getHashedPassword(user.getPassword()));
			this.em.persist(user);
		}
	}

	public boolean usernameTaken(String username) {
		return findByUsername(username) != null;
	}

	public boolean validateCredentials(String username, String password) {
		parameters.clear();
		parameters.put("username", username);
		parameters.put("password", getHashedPassword(password));
		return getListWithNamedQuery(QueryNames.User_ValidateCredentials, parameters).size() != 0;
	}

	public User findByUsername(String username) {
		parameters.clear();
		parameters.put("username", username);
		return getSignleResultWithNamedQuery(QueryNames.User_FindByUsername, parameters);
	}

	public void removeUserById(Long id) {
		remove(findById(id));
	}

	public String getHashedPassword(String password) {
		try {
			MessageDigest mda = MessageDigest.getInstance("SHA-512");
			password = new String(mda.digest(password.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}
	
	public Long getIdByName(String username){
		Query query = em.createQuery("SELECT id FROM User WHERE username:=name");
		query.setParameter("name", username);
		return (Long) query.getSingleResult();
	}
	
	public Long getMaxID(){
		Query query = em.createQuery("SELECT u.id FROM User u ORDER BY u.id DESC");
		query.setMaxResults(1);
		return (Long) query.getSingleResult();
	}
}
