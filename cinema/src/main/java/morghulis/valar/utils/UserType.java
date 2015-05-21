package morghulis.valar.utils;

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

import morghulis.valar.model.User;

@Entity
@Table(name = "ROLES")
@XmlRootElement
public class UserType implements Serializable {

	private static final long serialVersionUID = -8450910398556257180L;

	public static final String ADMINISTRATOR = "Administrator";
	public static final String CUSTOMER = "Customer";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(mappedBy = "userType", cascade = CascadeType.REMOVE)
	private List<User> users;
	
	private String name;
	
	public UserType() {
		this.name = CUSTOMER;
	}
	
	public UserType(String name) {
		this.name = name;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
