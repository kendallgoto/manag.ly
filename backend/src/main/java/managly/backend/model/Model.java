package managly.backend.model;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class Model {
	
	//PK functions for database management
	abstract public int getId();
	abstract public void setId(int id);
	
	public boolean equals(Model m) {
		if(this.getClass().equals(m.getClass())) {
			return (this.getId() == m.getId());
		}
		return false;
	}
}
