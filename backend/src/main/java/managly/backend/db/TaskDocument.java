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

public class TaskDocument extends Document<Task> {
	
	List<TaskDocument> subtasks;
	List<TeammateDocument> assignedTeammates;

	public TaskDocument() {
		super("tasks", Task.class, null, "taskid");
	}
	public TaskDocument(String taskNumber, String name, int projectId) {
		this();
		Task task = new Task(taskNumber, name, projectId);
		super.setObject(task);
	}
	public TaskDocument(String taskNumber, String name, int projectId, Integer parentId) {
		this();
		Task task = new Task(taskNumber, name, projectId, parentId);
		super.setObject(task);
	}
	
	protected boolean saveNew() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO "+tableName+"(`taskNumber`, `projectId`, `name`, `parentId`) VALUES(?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, backingObject.getTaskNumber());
        ps.setInt(2, backingObject.getProjectId());
        ps.setString(3, backingObject.getName());
        ps.setObject(4, backingObject.getParentId());
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
        PreparedStatement ps = conn.prepareStatement("UPDATE "+tableName+" SET taskNumber=?, parentId=?, completed=?, name=?, projectId=? WHERE taskId=?");
        ps.setString(1, backingObject.getTaskNumber());
        ps.setObject(2, backingObject.getParentId());
        ps.setBoolean(3, backingObject.isCompleted());
        ps.setString(4, backingObject.getName());
        ps.setInt(5, backingObject.getProjectId());
        ps.setInt(6, backingObject.getId());
        ps.executeUpdate();
        if(ps.getUpdateCount() == 1) {
    		return true;
        }
		return false;
	}

	protected Task generate(ResultSet res) throws SQLException {
		Task resulting = new Task(
				res.getString("taskNumber"),
				res.getString("name"),
				res.getInt("taskId"),
				res.getBoolean("completed"),
				res.getInt("projectId"),
				res.getInt("parentId")
		);
		return resulting;
	}
	private static List<TaskDocument> gather(String field, int id) {
		try {
			List<TaskDocument> result = new ArrayList<TaskDocument>();
			
	        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `tasks` WHERE "+field+" = ?;");
	        ps.setInt(1, id);
	        ResultSet resultSet = ps.executeQuery();
	    	while(resultSet.next()) {
	    		TaskDocument thisTask = new TaskDocument();
	    		thisTask.populateFromSet(resultSet);
	            result.add(thisTask);
	    	}
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	protected static List<TaskDocument> gatherByParent(int parentId) {
		return gather("parentId", parentId);
	}
	protected static List<TaskDocument> gatherByProject(int projectId) {
		return gather("projectId", projectId);
	}
	
	public int populateTeammates() {
		assignedTeammates = TeammateDocument.gatherByTask(backingObject.getId());
		return (assignedTeammates == null) ? 0 : assignedTeammates.size();
	}
	public List<TeammateDocument> getTeammates() {
		return assignedTeammates;
	}

	public int populateSubtasks() {
		subtasks = TaskDocument.gatherByParent(backingObject.getId());
		return (subtasks == null) ? 0 : subtasks.size();
	}
	public List<TaskDocument> getSubtasks() {
		return subtasks;
	}

	private static void gatherRunner(TaskDocument task) {
		task.populateSubtasks();
		for(TaskDocument subtask : task.getSubtasks()) {
			gatherRunner(subtask);
		}
	}
	public static void deepPopulate(ProjectDocument project) {
		for(TaskDocument task : project.getTasks()) {
			gatherRunner(task);
		}
	}



}
