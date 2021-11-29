package managly.backend.http;

public class ViewTeamViewRequest {
	int projectID;
	
	public int getArchiveProject() { return projectID; }
	public void setArchiveProject(int ID) { this.projectID = ID; }
		
	public ViewTeamViewRequest(int ID) {
		this.projectID = ID;
	}
	
	public String toString() {
		return "Team View(" + projectID +")";
	}

}
