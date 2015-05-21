package morghulis.valar.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
	@NamedQuery(name = "allScreenings", query = "SELECT s FROM Screening s"),
	@NamedQuery(name = "allScreeningsByHallNumber", query = "SELECT s FROM Screening s WHERE s.hall.hallNUmber = :hallNumber")
})
public class Screening implements Serializable {

	private static final long serialVersionUID = 2520912188484284800L;

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date screeningDate;
	@ManyToOne
	private Hall hall;
	
	@OneToOne
	private Movie movie;
	
	public Screening() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Screening(Hall hall, Movie movie) {
		super();
		this.hall = hall;
		this.movie = movie;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getScreeningDate() {
		return screeningDate;
	}
	public void setScreeningDate(Date screeningDate) {
		this.screeningDate = screeningDate;
	}
	public Hall getHall() {
		return hall;
	}
	public void setHall(Hall hall) {
		this.hall = hall;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
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
		if (getClass() != obj.getClass())
			return false;
		Screening other = (Screening) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Screening [hall=" + hall + ", movie=" + movie + "]";
	}
	
	
}
