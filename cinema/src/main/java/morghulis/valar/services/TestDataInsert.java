package morghulis.valar.services;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import morghulis.valar.utils.DatabaseUtils;

@Singleton
@Startup
public class TestDataInsert {

	@EJB
	private DatabaseUtils dbUtils;

	public TestDataInsert() {
		super();
	}
	
	@PostConstruct
	public void init(){
		dbUtils.addTestDataToDB();
	}
}
