package managly.backend.http;

public class UnassignTeammateRequest {

	int project;
	int task;
	
	public int getAssignedProject() { return project; }
	public void setAssignedProject(int project) { this.project = project; }
	public int getAssignedTask() { return task; }
	public void setAssignedTask(int task) {this.task = task; }
	
	public UnassignTeammateRequest(int p, int t) {
		this.project = p;
		this.task = t;
	}
	
	public UnassignTeammateRequest() {
	}
	
	public String toString() {
		return "Unassign task(" + project + "," + task +")";
	}
}
