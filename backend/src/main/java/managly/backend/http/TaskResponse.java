package managly.backend.http;

import managly.backend.model.Task;

public class TaskResponse extends Task implements ManaglyResponse {	
	public TaskResponse[] subTasks;
	public TeammateResponse[] assignedTeammates;
	
	public TaskResponse(Task o) { 
		super();
		
		this.setId(o.getId());
		this.setCompleted(o.isCompleted());
		this.setName(o.getName());
		this.setParentId(o.getParentId());
		this.setProjectId(o.getProjectId());
		this.setTaskNumber(o.getTaskNumber());
	}
	
	public String toString() {
		return "TaskResponse("+this.getName()+")";
	}

}
