package bg.cinema.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "HALLS")
public class Hall implements Serializable {

	private static final long serialVersionUID = -7868119437228188399L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int hallNumber;

	@ManyToOne
	private Movie movie;

	@OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
	private List<Seat> seats;

	public Hall() {

	}

	public Hall(Long id, int hallNumber) {
		super();
		this.hallNumber = hallNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getHallNumber() {
		return hallNumber;
	}

	public void setHallNumber(int hallNumber) {
		this.hallNumber = hallNumber;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

}
