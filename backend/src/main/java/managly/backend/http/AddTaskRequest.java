package managly.backend.http;

public class AddTaskRequest {
	int task;
	int parentTaskID;
	
	public int getTask() { return task; }
	public void setTask(int ID) { this.task = ID; }
	public int getParentTaskID() { return parentTaskID; }
	public void setParentTaskID(int ID) { this.parentTaskID = ID; }
	
	public AddTaskRequest(int t, int pTask) {
		this.task = t;
		this.parentTaskID = pTask;
	}
	
	public AddTaskRequest() {
	}
	
	public String toString() {
		return "Add Task(" + task +")";
	}

}
