package managly.backend.http;

import managly.backend.db.TeammateDocument;
import managly.backend.model.Teammate;

public class TeammateResponse extends Teammate implements ManaglyResponse {	
	public TaskResponse[] assignedTasks;
	
	public TeammateResponse(TeammateDocument o) { 
		super();
		
		this.setId(o.getObject().getId());
		this.setName(o.getObject().getName());
		this.setProjectId(o.getObject().getProjectId());
		this.assignedTasks = new TaskResponse[o.getAssignedTasks() == null ? 0 : o.getAssignedTasks().size()];
		for(int i = 0; i < assignedTasks.length; i++) {
			assignedTasks[i] = new TaskResponse(o.getAssignedTasks().get(i));
		}

	}
	
	public String toString() {
		return "TeammateResponse("+this.getName()+")";
	}

}
