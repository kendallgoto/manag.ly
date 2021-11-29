package managly.backend.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import managly.backend.model.Project;

public class ProjectDocument extends Document<Project> {
	
	List<TaskDocument> tasks;
	List<TeammateDocument> teammates;

	public ProjectDocument() {
		super("projects", Project.class, null, "projectId");
	}
	public ProjectDocument(String title) {
		this();
		Project pr = new Project(title);
		super.setObject(pr);
	}
	
	protected boolean saveNew() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO "+tableName+"(`title`) VALUES(?);", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, backingObject.getTitle());
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
        PreparedStatement ps = conn.prepareStatement("UPDATE "+tableName+" SET title=?, archived=? WHERE projectId=?");
        ps.setString(1, backingObject.getTitle());
        ps.setBoolean(2, backingObject.isArchived());
        ps.setInt(3, backingObject.getId());
        ps.executeUpdate();
        if(ps.getUpdateCount() == 1) {
    		return true;
        }
		return false;
	}

	protected Project generate(ResultSet res) throws SQLException {
		Project resulting = new Project(
				res.getString("title"),
				res.getInt("projectId"),
				res.getBoolean("archived")
		);
		return resulting;
	}
	
	public int populateTeammates() {
		// populate where teammate projectId = this.projectId
		teammates = TeammateDocument.gatherByProject(backingObject.getId());
		return (teammates == null) ? 0 : teammates.size();
	}
	public List<TeammateDocument> getTeammates() {
		return teammates;
	}

	public int populateTasks() {
		// populate where task projectId = this.projectId
		tasks = TaskDocument.gatherByProject(backingObject.getId());
		return (tasks == null) ? 0 : tasks.size();
	}
	public List<TaskDocument> getTasks() {
		return tasks;
	}

	public static List<ProjectDocument> gather() {
		try {
			List<ProjectDocument> result = new ArrayList<ProjectDocument>();
	        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `projects`");
	        ResultSet resultSet = ps.executeQuery();
	    	while(resultSet.next()) {
	    		ProjectDocument thisProject = new ProjectDocument();
	    		thisProject.populateFromSet(resultSet);
	            result.add(thisProject);
	    	}
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
