package morghulis.valar.utils;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import morghulis.valar.model.Ticket;

@Entity
@Table(name = "SEATSTATUS")
@XmlRootElement
public class SeatStatus {

	
	public static final String TAKEN = "Taken";
	public static final String  RESERVED = "Reserved";
	public static final String AVAILABLE = "Availbable";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String text;

	@OneToMany(mappedBy = "status", cascade = CascadeType.REMOVE)
	private List<Ticket> seats;
	
	public SeatStatus(){
		text = AVAILABLE;
	}
	public SeatStatus(String text){
		this.text = text;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Ticket> getSeats() {
		return seats;
	}

	public void setSeats(List<Ticket> seats) {
		this.seats = seats;
	}

	@Override
	public String toString(){
		return this.text;
	}
}
