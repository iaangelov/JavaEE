package morghulis.valar.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import morghulis.valar.dao.QueryNames;

@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = QueryNames.Screening_GetAllScreenings, query = "SELECT s FROM Screening s"),
	@NamedQuery(name = QueryNames.Screening_GetAllScreeningsByHallID, query = "SELECT s FROM Screening s WHERE s.hall.id = :id"),
	@NamedQuery(name = QueryNames.Screening_GetAllScreeningsByScreeningID, query = "SELECT s FROM Screening s WHERE s.id = :id"),
	@NamedQuery(name = QueryNames.Screening_GetAllScreeningsByMovieName, query = "SELECT s FROM Screening s WHERE s.movie.name = :name")
})
public class Screening implements Serializable {

	private static final long serialVersionUID = 2520912188484284800L;

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar screeningDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Hall hall;
	
	@ManyToOne
	private Movie movie;
	
	@OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Ticket> tickets; //new ArrayList()
	
	
	public Screening() {
		super();
	}
	public Screening(Hall hall, Movie movie, Calendar screeningDate) {
		super();
		this.hall = hall;
		this.movie = movie;
		this.screeningDate = screeningDate;

	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Calendar getScreeningDate() {
		return screeningDate;
	}
	public void setScreeningDate(Calendar screeningDate) {
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
		return "Screening [ Id=" + id + "hall=" + hall + ", movie=" + movie + "]";
	}
	
	
}
