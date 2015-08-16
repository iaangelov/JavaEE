package panayotov.week1.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import panayotov.week1.dao.QueryNames;
import panayotov.week1.utils.UserType;

@Entity
@XmlRootElement
@Table(name = "T_USER")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@NamedQueries({
		@NamedQuery(name = QueryNames.User_GetAllAsAdmin, query = "SELECT u FROM User u"),
		@NamedQuery(name = QueryNames.User_ValidateCredentials, query = "SELECT u FROM User u WHERE u.username=:username AND u.password=:password"),
		@NamedQuery(name = QueryNames.User_FindByUsername, query = "SELECT u FROM User u WHERE u.username=:username"),
		@NamedQuery(name = QueryNames.User_GetAllAsUser, query = "SELECT u FROM User u WHERE u.userType = 'Customer'") })
public class User implements Serializable {

	private static final long serialVersionUID = 8789553103516459097L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String username;

	private String password;

	private String email;
	
	private String first_name;
	
	private String surname;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Ticket> tickets;

	private String userType;

	public User() {
		this("", "", "", "", "", UserType.CUSTOMER);
	}

	public User(String username, String password, String email,String first_name, String surname, 
			UserType userType) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.first_name = first_name;
		this.surname = surname;
		this.userType = userType.getText();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Ticket> getTickets() {
		return this.tickets;
	}

	public void setTickets(final List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public UserType getUserType() {
		return UserType.getType(userType);
	}

	public void setUserStatus(UserType userType) {
		if (userType == null) {
			this.userType = null;
		} else {
			this.userType = userType.getText();
		}
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", email=" + email + ", tickets=" + tickets + ", userType="
				+ userType + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
