package managly.backend.http;

public class TeammateRequest {
	
	String teammate;
	int projectID;
	
	public int getTeammateRequestProject() { return projectID; }
	public void setTeammateRequestProject(int ID) { this.projectID = ID; }
	public String getTeammateRequest() { return teammate; }
	public void setTeammate(String addTeammate) { this.teammate = addTeammate; }
	
	public TeammateRequest(String t, int ID) {
		this.teammate = t;
		this.projectID = ID;
	}
	
	public TeammateRequest() {
	}
	
	public String toString() {
		return "Add Teammate(" + projectID +")";
	}

}
