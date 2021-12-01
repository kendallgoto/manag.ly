package managly.backend.http;

import managly.backend.db.TaskDocument;
import managly.backend.model.Task;

public class TaskResponse extends Task implements ManaglyResponse {	
	public TaskResponse[] subTasks;
	public TeammateResponse[] assignedTeammates;
	
	public TaskResponse(TaskDocument o) { 
		super();
		
		this.setId(o.getObject().getId());
		this.setCompleted(o.getObject().isCompleted());
		this.setName(o.getObject().getName());
		this.setParentId(o.getObject().getParentId());
		this.setProjectId(o.getObject().getProjectId());
		this.setTaskNumber(o.getObject().getTaskNumber());
	}
	
	public String toString() {
		return "TaskResponse("+this.getName()+")";
	}

}
