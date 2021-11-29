package managly.backend.http;

public class AssignTeammateRequest {
	int project;
	int teammate;
	
	public int getAssignedProject() { return project; }
	public void setAssignedProject(int project) { this.project = project; }
	public int getAssignedTeammate() { return teammate; }
	public void setAssignedTeammate(int teammate) {this.teammate = teammate; }
	
	public AssignTeammateRequest(int p, int t) {
		this.project = p;
		this.teammate = t;
	}
	
	public AssignTeammateRequest() {
	}
	
	public String toString() {
		return "Assign Teammate(" + project + "," + teammate +")";
	}
}
