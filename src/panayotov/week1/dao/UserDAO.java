package panayotov.week1.dao;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.eclipse.persistence.internal.jpa.EntityManagerFactoryImpl;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryProvider;

import panayotov.week1.models.User;
import panayotov.week1.services.UserContext;
import panayotov.week1.utils.UserType;

@Stateful
public class UserDAO extends GenericDAOImpl<User> {

	@Inject
	private UserContext userContext;

	private Map<String, String> parameters = new HashMap<>();

	public Collection<User> getAllUsers() {
		if (null != userContext.getCurrentUser() && userContext.getCurrentUser().getUserType() == UserType.ADMINISTRATOR) {
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
		return getListWithNamedQuery(QueryNames.User_ValidateCredentials,
				parameters).size() != 0;
	}

	public User findByUsername(String username) {
		parameters.clear();
		parameters.put("username", username);
		return getSignleResultWithNamedQuery(QueryNames.User_FindByUsername,
				parameters);
	}

	public void removeUserById(Long id) {
		remove(findById(id));
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
