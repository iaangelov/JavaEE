package bg.cinema.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "MOVIES")
@NamedQuery(name = "getAllMovies", query = "SELECT b FROM Movie b")
public class Movie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1684116948573676463L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	//... more movie describing fields could be added...
	
	private String type;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@OneToMany(mappedBy =  "movie")
	private List<Hall> halls;
	
	@OneToMany(mappedBy = "movie")
	private List<Ticket> tickets;  //tickets bought for projection, needed for 1.11
	
	@OneToMany(mappedBy = "movie")
	private List<Reservation> reservations; //reservations made for projection, needed for 1.5

	public Movie(String name, String type, Date time) {
		super();
		this.name = name;
		this.type = type;
		this.time = time;
	}

	public Movie() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
		
}
