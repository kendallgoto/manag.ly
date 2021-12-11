package managly.backend.http;

public class TaskRequest {
	String name;
	int projectId;
	int taskId;
	Integer taskParent;
	boolean markTask;
	
	public String getName() { return name; }
	public void setName(String name) {this.name = name; }
	public int getProjectId() { return projectId; }
	public void setProjectId(Integer ID) { this.projectId = ID; }
	public Integer getTaskParent() { return taskParent; }
	public void setTaskParent(int taskParent) { this.taskParent = taskParent; }
	public boolean getMarkTask() { return markTask; }
	public void setMarkTask(boolean markTask) { this.markTask = markTask; }
	public void setTaskId(int taskId) { this.taskId = taskId; }
	public int getTaskId() { return taskId; }

	
	public TaskRequest(String name, int projectId, int taskParent) {
		this.name = name;
		this.projectId = projectId;
		this.taskParent = taskParent;
	}
	public TaskRequest(String name, int projectId) {
		this.name = name;
		this.projectId = projectId;
	}
	public TaskRequest() {}
	
	public String toString() {
		return "Add Task("+name+","+projectId+","+taskParent+")";
	}

}
