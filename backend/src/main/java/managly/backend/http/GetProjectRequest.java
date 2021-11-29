package managly.backend.http;

public class GetProjectRequest {
	String project;
	
	public String getProject() { return project; }
	public void setProject(String newProject) { this.project = newProject; }
	
	public String toString() {
		return "" + project;
	}

	
	public GetProjectRequest(String newProject) {
		this.project = newProject;
	}
	
	public GetProjectRequest() {
	}

}
