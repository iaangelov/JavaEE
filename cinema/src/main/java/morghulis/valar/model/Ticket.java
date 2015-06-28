package morghulis.valar.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import morghulis.valar.dao.QueryNames;
import morghulis.valar.utils.SeatStatus;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = QueryNames.Ticket_GetAllTickets, query = "SELECT t FROM Ticket t"),
		@NamedQuery(name = QueryNames.Ticket_FindByUserID, query = "SELECT t FROM Ticket t WHERE t.user.id = :userId"),
		@NamedQuery(name = QueryNames.Ticket_FindReservedByUserID, query = "SELECT t FROM Ticket t WHERE t.status = :statusReserved AND t.user.id = :userId"),
		@NamedQuery(name = QueryNames.Ticket_FindByUserNames, query = "SELECT t FROM Ticket t WHERE t.user.first_name = :fname AND t.user.surname = :lname"),
		@NamedQuery(name = QueryNames.Ticket_FindByScreeningID, query = "SELECT t FROM Ticket t WHERE t.screening.id = :screeningId") })
public class Ticket implements Serializable {

	private static final long serialVersionUID = 1694579078976547199L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(cascade = CascadeType.DETACH)
	private Screening screening;

	@ManyToOne
	private User user;

	private int seatNumber;

	private String status;

	public Ticket() {

	}

	public Ticket(Screening screening, User user, int seat, SeatStatus status) {
		this.screening = screening;
		this.user = user;
		this.seatNumber = seat;
		this.status = status.getText();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Screening getScreening() {
		return screening;
	}

	public void setScreening(Screening screening) {
		this.screening = screening;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SeatStatus getStatus() {
		return SeatStatus.getType(status);
	}

	public void setStatus(SeatStatus status) {
		if (status == null) {
			this.status = null;

		} else {
			this.status = status.getText();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Ticket))
			return false;
		Ticket other = (Ticket) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ticket  screening=" + screening + ", user=" + user
				+ ", seatNumber=" + seatNumber + " status= " + status + "]";
	}
}
