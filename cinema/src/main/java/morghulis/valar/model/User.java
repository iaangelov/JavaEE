package morghulis.valar.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import morghulis.valar.utils.UserType;

@Entity
@XmlRootElement
public class User implements Serializable {

	private static final long serialVersionUID = 8789553103516459097L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String username;

	private String password;

	private String email;
	
	@OneToMany(mappedBy = "user")
	private List<Ticket> tickets;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="USER_ID")
	private UserType userType;


	public User() {
	}

	public User(String username, String password, String email, UserType userType) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.userType = userType;
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

	public List<Ticket> getTickets() {
		return this.tickets;
	}

	public void setTickets(final List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	public UserType getUserType() {
		return userType;
	}

	public void setUserStatus(UserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (username != null && !username.trim().isEmpty())
			result += "userName: " + username;
		if (password != null && !password.trim().isEmpty())
			result += ", password: " + password;
		if (email != null && !email.trim().isEmpty())
			result += ", email: " + email;
		return result;
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
