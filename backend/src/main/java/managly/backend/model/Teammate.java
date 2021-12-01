package managly.backend.model;


public class Teammate extends Model {
	
	int teammateId;
	String name;
	int projectId;
	
	public Teammate(String name, int pId) {
		this.name = name;
		this.projectId = pId;
	}
	public Teammate(int tId, String name, int pId) {
		this(name, pId);
		this.teammateId = tId;
	}
	
	protected Teammate() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getId() {
		return teammateId;
	}
	public void setId(int id) {
		this.teammateId = id;
	}

}
