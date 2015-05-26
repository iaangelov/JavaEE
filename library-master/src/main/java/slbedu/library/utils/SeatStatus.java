package slbedu.library.utils;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import slbedu.library.model.Ticket;

@Entity
@XmlRootElement
public class SeatStatus  implements Serializable{

	
	/**
     * 
     */
    private static final long serialVersionUID = -7199482655722818223L;
    
    public static final String TAKEN = "Taken";
	public static final String  RESERVED = "Reserved";
	public static final String AVAILABLE = "Availbable";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String text;

//	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="status")
//	private List<Ticket> seats;
	
	public SeatStatus() {
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

//	public List<Ticket> getSeats() {
//		return seats;
//	}
//
//	public void setSeats(List<Ticket> seats) {
//		this.seats = seats;
//	}
}
