package managly.backend.http;

import managly.backend.model.Project;

public class ProjectResponse extends Project implements ManaglyResponse {	
	public ProjectResponse(Project o) { 
		super("");
		this.setTitle(o.getTitle());
		this.setArchived(o.isArchived());
		this.setId(o.getId());
	}
	
	public String toString() {
		return "ProjectResponse()";
	}

}
