package managly.backend.http;

import managly.backend.model.Teammate;

public class TeammateResponse extends Teammate implements ManaglyResponse {	
	public TeammateResponse[] subTasks;
	public TeammateResponse[] assignedTeammates;
	
	public TeammateResponse(Teammate o) { 
		super();
		
		this.setId(o.getId());
		this.setName(o.getName());
		this.setProjectId(o.getProjectId());
	}
	
	public String toString() {
		return "TeammateResponse("+this.getName()+")";
	}

}
