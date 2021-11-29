package managly.backend.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import managly.backend.model.Model;

abstract class Document<T extends Model> {
	static java.sql.Connection conn;

	String tableName = null;
	Class<T> backingClass;
	T backingObject;
	String primaryKey = null;
	
	static {
		try {
			conn = DatabaseUtil.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Document(String tableName, Class<T> backingClass, T backingObject, String primaryKey) {
		this.tableName = tableName;
		this.backingClass = backingClass;
		this.backingObject = backingObject;
		this.primaryKey = primaryKey;
	}
	protected void populateFromSet(ResultSet set) throws SQLException {
		T result = this.generate(set);
		this.backingObject = result;
	}
	public boolean findById(int documentId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE "+primaryKey+" = ?;");
        ps.setInt(1, documentId);
        ResultSet resultSet = ps.executeQuery();
    	if(resultSet.next()) {
    		populateFromSet(resultSet);
            return true;
    	} else {
    		return false;
    	}
	}
	
	public boolean save() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE "+primaryKey+" = ?;");
        ps.setInt(1, backingObject.getId());
        ResultSet resultSet = ps.executeQuery();
    	if(resultSet.next()) {
    		return this.updateExisting();
    	} else {
    		return this.saveNew();
    	}
	}
	
	protected abstract T generate(ResultSet res) throws SQLException;
	abstract protected boolean saveNew() throws SQLException;
	abstract protected boolean updateExisting() throws SQLException;
	
	public boolean delete() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM " + tableName + " WHERE "+primaryKey+" = ?;");
        ps.setInt(1, backingObject.getId());
        ps.executeUpdate();
    	if(ps.getUpdateCount() == 1) {
    		setObject(null);
            return true;
    	} else {
    		return false;
    	}
	}
	
	public T getObject() {
		return backingObject;
	}
	public void setObject(T newObj) {
		backingObject = newObj;
	}
}
