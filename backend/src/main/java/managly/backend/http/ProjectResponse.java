package managly.backend.http;

import java.util.List;

import managly.backend.db.ProjectDocument;
import managly.backend.db.TaskDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.model.Project;

public class ProjectResponse extends Project implements ManaglyResponse {	
	public TaskResponse[] tasks;
	public TeammateResponse[] teammates;
	
	public ProjectResponse(ProjectDocument o) { 
		super();
		this.setTitle(o.getObject().getTitle());
		this.setArchived(o.getObject().isArchived());
		this.setId(o.getObject().getId());
		
		List<TaskDocument> tasks = o.getTasks();
		List<TeammateDocument> teammates = o.getTeammates();
		
		if(tasks != null) {
			this.tasks = new TaskResponse[tasks.size()];
			for(int i = 0; i < tasks.size(); i++) {
				this.tasks[i] = new TaskResponse(tasks.get(i));
			}
		}
		else
			this.tasks = new TaskResponse[0];

		if(teammates != null) {
			this.teammates = new TeammateResponse[teammates.size()];
			for(int i = 0; i < tasks.size(); i++) {
				this.teammates[i] = new TeammateResponse(teammates.get(i));
			}
		}
		else
			this.teammates = new TeammateResponse[0];
	}
	
	public String toString() {
		return "ProjectResponse("+this.getTitle()+")";
	}

}
