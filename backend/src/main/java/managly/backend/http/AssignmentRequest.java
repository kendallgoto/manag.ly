package managly.backend.http;

public class AssignmentRequest {
	int teammateId;
	int taskId;
	public void setTeammateId(int teammateID) { this.teammateId = teammateID; }
	public int getTeammateId() { return teammateId; }
	public void setTaskId(int taskID) { this.taskId = taskID; }
	public int getTaskId() { return taskId; }
	
	public AssignmentRequest(int teammateId, int taskId) {
		this.teammateId = teammateId;
		this.taskId  = taskId;
		
	}
	public AssignmentRequest() {}

	public String toString() {
		return "AssignmentRequest("+teammateId+", "+taskId+")";
	}
}
