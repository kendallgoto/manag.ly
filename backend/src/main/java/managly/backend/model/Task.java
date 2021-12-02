package managly.backend.model;

public class Task extends Model {
	int taskId;
	String taskNumber;
	Integer parentId; // can be null; need to use Integer
	boolean completed;
	String name;
	int projectId;
	
	//for creation
	public Task(String taskNumber, String name, int projectId) {
		this.taskNumber = taskNumber;
		this.name = name;
		this.completed = false;
		this.projectId = projectId;
		this.parentId = null;
	}
	public Task(String taskNumber, String name, int projectId, Integer parentId) {
		this(taskNumber, name, projectId);
		this.parentId = parentId;
	}
	
	//for reloading
	public Task(String taskNumber, String name, int taskId, boolean completed, int projectId){
		this(taskNumber, name, projectId);
		this.completed = completed;
		this.taskId = taskId;
	}
	public Task(String taskNumber, String name, int taskId, boolean completed, int projectId, Integer parentId) {
		this(taskNumber, name, taskId, completed, projectId);
		this.parentId = parentId;
	}
	
	protected Task() {}
	
	public String getTaskNumber() {
		return taskNumber;
	}
	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public int getId() {
		return taskId;
	}
	public void setId(int id) {
		this.taskId = id;
	}

}
