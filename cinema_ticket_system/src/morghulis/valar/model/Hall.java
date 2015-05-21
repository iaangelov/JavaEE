package morghulis.valar.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "HALLS")
public class Hall implements Serializable {

	private static final long serialVersionUID = -7868119437228188399L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hallNumber;
	
	//@OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
	private List<Ticket> seats;
	
	public Hall(){
		
	}

	public List<Ticket> getSeats() {
		return seats;
	}

	public void setSeats(List<Ticket> seats) {
		this.seats = seats;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hallNumber == null) ? 0 : hallNumber.hashCode());
		result = prime * result + ((seats == null) ? 0 : seats.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hall other = (Hall) obj;
		if (hallNumber == null) {
			if (other.hallNumber != null)
				return false;
		} else if (!hallNumber.equals(other.hallNumber))
			return false;
		if (seats == null) {
			if (other.seats != null)
				return false;
		} else if (!seats.equals(other.seats))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Hall [hallNumber=" + hallNumber + ", seats=" + seats + "]";
	}
	
	
	
	


	
}
