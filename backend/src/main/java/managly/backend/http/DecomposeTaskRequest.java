package managly.backend.http;

public class DecomposeTaskRequest {
	int task;
	String[] subTasks;
	
	public int getTask() { return task; }
	public void setTask(int ID) { this.task = ID; }

	public DecomposeTaskRequest(int t, String[] subTasks) {
		this.task = t;
		this.subTasks = subTasks;
	}
	
	public DecomposeTaskRequest() {
	}
	
	public String toString() {
		return "Decompose Task(" + task +")";
	}

}
