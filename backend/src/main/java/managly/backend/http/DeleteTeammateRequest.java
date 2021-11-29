package managly.backend.http;

public class DeleteTeammateRequest {
	
	String teammate;	// Schema says int but are we storing teammate's names as ints?
	
	public String getTeammateRequest() { return teammate; }
	public void setTeammate(String addTeammate) { this.teammate = addTeammate; }
	
	public DeleteTeammateRequest(String t) {
		this.teammate = t;
	}
	
	public DeleteTeammateRequest() {
	}
	

}
