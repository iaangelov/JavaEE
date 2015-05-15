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
@Table(name = "SEATS")
public class Seat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1379529305754673550L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int seatNumber;
	
	private int timeToKeepReserved; // int minutes ili ne6to po interesno??
		
	@ManyToOne
	private Hall hall;
	
	
	//nujno li e? ideqta e 4e poprincip ticketi ima samo za mesta sus status taken, a ne za vsi4ki mesta.
	//Oba4e susto taka mesta i bileti v DB sa OneToOne, i dvete se zapisvat samo ako reservation e processed,
	//toest togava seat status susto se smenq na taken
	@OneToOne
	private Ticket ticket;
	
	@ManyToOne
	private SeatStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public int getTimeToKeepReserved() {
		return timeToKeepReserved;
	}

	public void setTimeToKeepReserved(int timeToKeepReserved) {
		this.timeToKeepReserved = timeToKeepReserved;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public SeatStatus getStatus() {
		return status;
	}

	public void setStatus(SeatStatus status) {
		this.status = status;
	}

	
}
