package bg.cinema.models;

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


//ne znam dali ste polzvame tova entity ili 6te go mahnem - prosto e edna ideq za ogranizaciq na statusite koqto 6te obsudim
// moje prosto da slojim edno string polence v seat, moje i s entity - ne znam koe e po udobno za sega
@Entity
@XmlRootElement
@Table(name = "SEATSTATUSES")
public class SeatStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6640438387452838302L;
	
	public static final String TAKEN = "taken";
	public static final String FREE = "FREE";
	public static final String RESERVED = "RESERVED";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "status", cascade = CascadeType.REMOVE)
	private List<Seat> seats;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

	
}
