package managly.backend.http;

public class AssignmentRequest {
	int teammateID;
	public void setTeammateID(int teammateID) { this.teammateID = teammateID; }
	public int getTeammateID() { return teammateID; }
	
	public AssignmentRequest(int teammateID) {
		this.teammateID = teammateID;
	}
	public AssignmentRequest() {}
	
	public String toString() {
		return "AssignmentRequest("+teammateID+")";
	}
}
