package managly.backend.http;

public class GetProjectRequest {
	int projectId;
	
	public void setProjectId(int projectId) { this.projectId = projectId; }
	public int getProjectId() { return this.projectId; }
		
	GetProjectRequest() {}
	
	public String toString() {
		return "GetProjectRequest("+projectId+")";
	}
}
