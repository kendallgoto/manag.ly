package managly.backend.http;

public class AssignmentRequest {
	int project;
	int teammate;
	
	public int getAssignedProject() { return project; }
	public void setAssignedProject(int project) { this.project = project; }
	public int getAssignedTeammate() { return teammate; }
	public void setAssignedTeammate(int teammate) {this.teammate = teammate; }
	
	public AssignmentRequest(int p, int t) {
		this.project = p;
		this.teammate = t;
	}
	
	public AssignmentRequest() {
	}
	
	public String toString() {
		return "AssignmentRequest(" + project + "," + teammate +")";
	}
}
