package bg.cinema.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "TICKETS")
public class Ticket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4157996108395482358L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private double price;

	@ManyToOne
	private Movie movie;

	@ManyToOne
	private Hall hall;

	@OneToOne
	private Seat seat;

	@ManyToOne
	private User buyer;

	public Ticket(double price) {
		super();
		this.price = price;
	}

	public Ticket() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

}
