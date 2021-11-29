package managly.backend.http;

public class CreateProjectRequest {
	String project;
	
	public String getProject() { return project; }
	public void setProject(String newProject) { this.project = newProject; }
	
	public String toString() {
		return "" + project;
	}

	
	public CreateProjectRequest(String newProject) {
		this.project = newProject;
	}
	
	public CreateProjectRequest() {
	}
	
	

}
