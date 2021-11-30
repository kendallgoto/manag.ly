package managly.backend.http;

import managly.backend.model.Project;

public class ProjectResponse extends Project implements ManaglyResponse {
	public int statusCode;
	
	public int getStatusCode() { return statusCode; };
	
	public ProjectResponse(int code, Project o) { 
		super("");
		this.setTitle(o.getTitle());
		this.setArchived(o.isArchived());
		this.setId(o.getId());
		statusCode = code;
	}
	
	public String toString() {
		return "ProjectResponse()";
	}

}
