package managly.backend.http;

public class DecomposeRequest {
	TaskRequest[] subtasks;
	int taskId;
	
	public TaskRequest[] getSubtasks() { return subtasks; }
	public void setSubtasks(TaskRequest[] subtasks) { this.subtasks = subtasks; }
	public int getTaskId() { return taskId; }
	public void setTaskId(int taskId) { this.taskId = taskId; }
	
	public DecomposeRequest(TaskRequest[] subtasks) { this.subtasks = subtasks; }
	
	public DecomposeRequest() {}
	
	public String toString() {
		return "Decompose Task("+subtasks+")";
	}
}