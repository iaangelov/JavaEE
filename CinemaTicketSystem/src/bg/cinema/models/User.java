package bg.cinema.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "USERS")
public class User implements Serializable {

	private static final long serialVersionUID = -7196507424378163030L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String userName;

	private String password;

	private String threeNames;

	private String email;

	@ManyToOne
	private UserType userType;

	@OneToMany(mappedBy = "buyer")
	private List<Ticket> ticketsBought;

	@OneToMany(mappedBy = "customer")
	private List<Reservation> reservations;

	public User() {

	}

	public User(String userName, String password, String threeNames,
			String email) {
		super();
		this.userName = userName;
		this.password = password;
		this.threeNames = threeNames;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getThreeNames() {
		return threeNames;
	}

	public void setThreeNames(String threeNames) {
		this.threeNames = threeNames;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public List<Ticket> getTicketsBought() {
		return ticketsBought;
	}

	public void setTicketsBought(List<Ticket> ticketsBought) {
		this.ticketsBought = ticketsBought;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

}
