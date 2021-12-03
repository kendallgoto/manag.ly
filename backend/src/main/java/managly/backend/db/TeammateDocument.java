package managly.backend.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import managly.backend.model.Project;
import managly.backend.model.Task;
import managly.backend.model.Teammate;

public class TeammateDocument extends Document<Teammate> {
	
	ArrayList<Task> assignedTasks;

	public TeammateDocument() {
		super("teammates", Teammate.class, null, "teammateId");
	}
	public TeammateDocument(String name, int pId) {
		this();
		Teammate teammate = new Teammate(name, pId);
		super.setObject(teammate);
	}
	
	protected boolean saveNew() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO "+tableName+"(`name`, `projectId`) VALUES(?, ?);", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, backingObject.getName());
        ps.setInt(2, backingObject.getProjectId());
        ps.executeUpdate();
        ResultSet res = ps.getGeneratedKeys();
        if(res.next()) {
        	int newId = res.getInt(1);
        	findById(newId);
    		return true;
        }
        return false;
	}
	
	protected boolean updateExisting() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE "+tableName+" SET name=?, projectId=? WHERE teammateId=?");
        ps.setString(1, backingObject.getName());
        ps.setInt(2, backingObject.getProjectId());
        ps.setInt(3, backingObject.getId());
        ps.executeUpdate();
        if(ps.getUpdateCount() == 1) {
    		return true;
        }
		return false;
	}

	protected Teammate generate(ResultSet res) throws SQLException {
		Teammate resulting = new Teammate(
				res.getInt("teammateId"),
				res.getString("name"),
				res.getInt("projectId")
		);
		return resulting;
	}
	private static ArrayList<TeammateDocument> gather(String field, int id) {
		try {
			ArrayList<TeammateDocument> result = new ArrayList<TeammateDocument>();
			
	        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `teammates` WHERE "+field+" = ? ORDER BY teammateId;");
	        ps.setInt(1, id);
	        ResultSet resultSet = ps.executeQuery();
	    	while(resultSet.next()) {
	    		TeammateDocument thisTeammate = new TeammateDocument();
	    		thisTeammate.populateFromSet(resultSet);
	            result.add(thisTeammate);
	    	}
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static List<TeammateDocument> gatherByTask(int taskId) {
		try {
			ArrayList<TeammateDocument> result = new ArrayList<TeammateDocument>();
			
	        PreparedStatement ps = conn.prepareStatement("SELECT * from `teammates` WHERE `teammateid` IN (SELECT `teammateid` FROM `taskAssignments` WHERE `taskid`=?)");
	        ps.setInt(1, taskId);
	        ResultSet resultSet = ps.executeQuery();
	    	while(resultSet.next()) {
	    		TeammateDocument thisTeammate = new TeammateDocument();
	    		thisTeammate.populateFromSet(resultSet);
	            result.add(thisTeammate);
	    	}
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static List<TeammateDocument> gatherByProject(int projectId) {
		return gather("projectId", projectId);
	}
	public boolean delete() throws SQLException {
		//TODO: delete TaskAssignments
        PreparedStatement ps = conn.prepareStatement("DELETE FROM `teammates` WHERE `teammateId` = ?;");
        ps.setInt(1, backingObject.getId());
        ps.executeUpdate();
    	if(ps.getUpdateCount() == 1) {
    		setObject(null);
            return true;
    	} else {
    		return false;
    	}
	}
	

}
