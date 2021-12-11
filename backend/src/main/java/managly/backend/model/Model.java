package managly.backend.model;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class Model {
	
	//PK functions for database management
	abstract public int getId();
	abstract public void setId(int id);
	
	@Override
	public boolean equals(Object o) {
		if(this.getClass().equals(o.getClass())) {
			return (this.getId() == ((Model)o).getId());
		}
		return false;
	}
}
