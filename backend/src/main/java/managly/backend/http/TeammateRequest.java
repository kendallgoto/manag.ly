package managly.backend.http;

public class TeammateRequest {
	
	String name;
	int projectID;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public int getProjectID() { return projectID; }
	public void setProjectID(int ID) { this.projectID = ID; }
	
	public TeammateRequest(String name, int ID) {
		this.name = name;
		this.projectID = ID;
	}
	
	public TeammateRequest() {}
	
	public String toString() {
		return "Add Teammate("+name+","+projectID+")";
	}

}
