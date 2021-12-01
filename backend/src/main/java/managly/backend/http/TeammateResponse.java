package managly.backend.http;

import managly.backend.db.TeammateDocument;
import managly.backend.model.Teammate;

public class TeammateResponse extends Teammate implements ManaglyResponse {	
	public TeammateResponse[] subTasks;
	public TeammateResponse[] assignedTeammates;
	
	public TeammateResponse(TeammateDocument o) { 
		super();
		
		this.setId(o.getObject().getId());
		this.setName(o.getObject().getName());
		this.setProjectId(o.getObject().getProjectId());
	}
	
	public String toString() {
		return "TeammateResponse("+this.getName()+")";
	}

}
