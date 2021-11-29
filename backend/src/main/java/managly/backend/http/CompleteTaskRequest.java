package managly.backend.http;

public class CompleteTaskRequest {

	int task;
	boolean complete;
	
	public int getTask() { return task; }
	public void setTask(int ID) { this.task = ID; }
	public boolean getCompletion() { return complete; }
	public void setCompletion(boolean status) { this.complete = status; }

	public CompleteTaskRequest(int t, boolean status) {
		this.task = t;
		this.complete = status;
	}
	
	public CompleteTaskRequest() {
	}
	
	public String toString() {
		return "Complete Task(" + task +")";
	}
}
