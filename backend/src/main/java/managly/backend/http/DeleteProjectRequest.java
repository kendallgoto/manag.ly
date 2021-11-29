package managly.backend.http;

public class DeleteProjectRequest {
	int projectID;
	
	public int getArchiveProject() { return projectID; }
	public void setArchiveProject(int ID) { this.projectID = ID; }
	
	
	public DeleteProjectRequest(int ID) {
		this.projectID = ID;
	}
	
	
	public String toString() {
		return "Delete Project(" + projectID +")";
	}
	
	
}
