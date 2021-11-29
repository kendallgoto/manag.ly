package managly.backend.http;

public class DecomposeTaskRequest {
	int task;
	
	public int getTask() { return task; }
	public void setTask(int ID) { this.task = ID; }

	public DecomposeTaskRequest(int t) {
		this.task = t;
	}
	
	public DecomposeTaskRequest() {
	}
	
	public String toString() {
		return "Decompose Task(" + task +")";
	}

}
