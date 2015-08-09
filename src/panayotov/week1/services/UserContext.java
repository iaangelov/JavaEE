package panayotov.week1.services;

import java.io.Serializable;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;

import panayotov.week1.models.User;

@SessionScoped
public class UserContext implements Serializable {

	private static final long serialVersionUID = -1286358449698328966L;

	private User currentUser;
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}