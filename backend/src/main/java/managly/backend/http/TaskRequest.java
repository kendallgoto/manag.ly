package managly.backend.http;

public class TaskRequest {
	String taskName;
	int projectID;
	int taskParent;
	
	public String getTaskName() { return taskName; }
	public void setTaskName(String taskName) {this.taskName = taskName; }
	public int getProjectID() { return projectID; }
	public void setTaskID(int ID) { this.projectID = ID; }
	public int getTaskParent() { return taskParent; }
	public void setTaskParent(int taskParent) { this.taskParent = taskParent; }
	
	
	public TaskRequest(int t, int pTask) {
		this.projectID = t;
		this.taskParent = pTask;
	}
	
	public TaskRequest() {}
	
	public String toString() {
		return "Add Task(" + projectID +")";
	}

}
