package managly.backend.http;

import managly.backend.db.TaskDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.model.Task;

public class TaskResponse extends Task implements ManaglyResponse {	
	public TaskResponse[] subtasks;
	public TeammateResponse[] assignedTeammates;
	
	public TaskResponse(TaskDocument o) { 
		super();
		
		this.setId(o.getObject().getId());
		this.setCompleted(o.getObject().isCompleted());
		this.setName(o.getObject().getName());
		this.setParentId(o.getObject().getParentId());
		this.setProjectId(o.getObject().getProjectId());
		this.setTaskNumber(o.getObject().getTaskNumber());
		this.subtasks = new TaskResponse[o.getSubtasks() == null ? 0 : o.getSubtasks().size()];
		for(int i = 0; i < subtasks.length; i++) {
			subtasks[i] = new TaskResponse(o.getSubtasks().get(i));
		}
		
		this.assignedTeammates = new TeammateResponse[o.getTeammates() == null ? 0 : o.getTeammates().size()];
		for(int i = 0; i < assignedTeammates.length; i++) {
			assignedTeammates[i] = new TeammateResponse(o.getTeammates().get(i));
		}
	}
	
	public String toString() {
		return "TaskResponse("+this.getName()+")";
	}

}
