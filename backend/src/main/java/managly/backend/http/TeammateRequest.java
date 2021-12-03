package managly.backend.http;

public class TeammateRequest {
	
	String name;
	int projectId;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getProjectId() { return projectId; }
	public void setProjectId(int ID) { this.projectId = ID; }
	
	public TeammateRequest(String name, int ID) {
		this.name = name;
		this.projectId = ID;
	}
	
	public TeammateRequest() {}
	
	public String toString() {
		return "Add Teammate("+name+","+projectId+")";
	}

}
