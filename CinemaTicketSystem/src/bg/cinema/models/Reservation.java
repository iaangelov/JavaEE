package bg.cinema.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "RESERVATIONS")
public class Reservation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1281502258642366743L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String reservationStatus;  //pending or processed
	//processed means that it is paid, which means that the seat is taken and together with ticket, seat goes into DB
	// when reservation is switched to the processed status magic will happen
	//keep it like that , make it enum, make another entity for it??? suggestions 
	
	@ManyToOne
	private User customer;
	
	@ManyToOne
	private Movie movie;
	
	//trqbva da znaem za koe mqsto e rezervaciqta, no ne trqbva tova mqsto da e v DB, zastoto rezercaiqta ne e processed 
		// kak za referirame mqstoto?
		//ideq - hall id + seat number , halls sa v db , posle ot seat number 6te znaem koe mqsto eventualno da dobavim

	public Reservation(String reservationStatus) {
		super();
		this.reservationStatus = reservationStatus;
	}
	
	public Reservation() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(String reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

}
