package morghulis.valar.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Screening implements Serializable {

	private static final long serialVersionUID = 2520912188484284800L;

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	
	private Hall hall;
	
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
