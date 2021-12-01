package managly.backend.http;

import managly.backend.model.Project;

import java.util.List;

import managly.backend.db.ProjectDocument;

public class ProjectResponseArray implements ManaglyResponse {
	public ProjectResponse[] projects;
	public ProjectResponseArray(List<ProjectDocument> projects) { 
		this.projects = new ProjectResponse[projects.size()];
		for(int i = 0; i < this.projects.length; i++) {
			this.projects[i] = new ProjectResponse(projects.get(i).getObject());
		}
	}
	
	public String toString() {
		return "ProjectResponseArray["+projects.length+"]";
	}

}
