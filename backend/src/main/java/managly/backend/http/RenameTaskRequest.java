package managly.backend.http;

public class RenameTaskRequest {
	
	int task;
	// how are we dealing with renaming tasks?
	
	public int getTask() { return task; }
	public void setTask(int ID) { this.task = ID; }

	public RenameTaskRequest(int t) {
		this.task = t;
	}
	
	public RenameTaskRequest() {
	}
	
	public String toString() {
		return "Rename Task(" + task +")";
	}


}
