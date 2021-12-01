package managly.backend.http;

public class ProjectRequest {
	String title;
	int projectId;
	
	public void setTitle(String title) { this.title = title; }
	public String getTitle() { return this.title; }
	public void setProjectId(int projectId) { this.projectId = projectId; }
	public int getProjectId() { return this.projectId; }
	
	ProjectRequest(String title) { this.title = title; }
	
	ProjectRequest() {}
	
	public String toString() {
		return "ProjectRequest("+title+")";
	}
}
