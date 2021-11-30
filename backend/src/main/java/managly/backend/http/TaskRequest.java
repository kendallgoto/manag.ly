package managly.backend.http;

public class TaskRequest {
	String name;
	int projectID;
	int taskParent;
	
	public String getName() { return name; }
	public void setName(String name) {this.name = name; }
	public int getProjectID() { return projectID; }
	public void setProjectID(int ID) { this.projectID = ID; }
	public int getTaskParent() { return taskParent; }
	public void setTaskParent(int taskParent) { this.taskParent = taskParent; }
	
	
	public TaskRequest(String name, int projectId, int taskParent) {
		this.name = name;
		this.projectID = projectId;
		this.taskParent = taskParent;
	}
	public TaskRequest(String name, int projectId) {
		this.name = name;
		this.projectID = projectId;
	}
	public TaskRequest() {}
	
	public String toString() {
		return "Add Task("+name+","+projectID+","+taskParent+")";
	}

}
