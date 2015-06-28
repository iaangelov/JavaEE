package morghulis.valar.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import morghulis.valar.dao.QueryNames;


@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = QueryNames.Reservation_GetAllReservations, query = "SELECT r FROM Reservation r"),
	//@NamedQuery(name = QueryNames.Reservation_FindReservationsByUserId, query = "SELECT r FROM Reservation r WHERE r.tickets.user.id = :userId")
	//vtoroto query ne e nujno - 6te vzimame reservationByUserId, KATO polzvame getBYUserID na purviq ticket ot rezervaciqta
	//tova ozna4ava susto taka 4e nqma nujda da pomnim list ot rezervacii v usera,
})
public class Reservation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029899214268313822L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(mappedBy = "reservation")
	private List<Ticket> tickets;
	
	public Reservation() { }

	public Reservation(List<Ticket> tickets) {
		super();
		this.tickets = tickets;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tickets == null) ? 0 : tickets.hashCode());
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
		Reservation other = (Reservation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tickets == null) {
			if (other.tickets != null)
				return false;
		} else if (!tickets.equals(other.tickets))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reservation [reservation with tickets=" + tickets + "]";
	};
}
