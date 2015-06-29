package morghulis.valar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import morghulis.valar.dao.QueryNames;

@Entity
@XmlRootElement
@Table(name = "HALLS")
@NamedQueries({
	@NamedQuery(name = QueryNames.Hall_GetAllHalls, query = "SELECT h FROM Hall h")
})
public class Hall implements Serializable {

	private static final long serialVersionUID = -7868119437228188399L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int hallNumber;
	
	@OneToMany(mappedBy = "hall", cascade = CascadeType.PERSIST)
	private List<Screening> screenings = new ArrayList<Screening>(); // new 
	
	public Hall() {
		// TODO Auto-generated constructor stub
	}
	
	public Hall(int hallNumber) {
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

	
	public List<Screening> getScreenings() {
		return screenings;
	}

	public void setScreenings(List<Screening> screenings) {
		this.screenings = screenings;
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
		Hall other = (Hall) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Hall [id=" + id + ", hallNumber=" + hallNumber
				+ ", screenings=" + screenings + "]";
	}

	
	
	
}
