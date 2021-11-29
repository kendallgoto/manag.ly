package managly.backend.http;

public class ArchiveProjectRequest {
	int projectID;
	
	public int getArchiveProject() { return projectID; }
	public void setArchiveProject(int ID) { this.projectID = ID; }
	
	
	public ArchiveProjectRequest(int ID) {
		this.projectID = ID;
	}
	
	
	public String toString() {
		return "Archive(" + projectID +")";
	}
}
