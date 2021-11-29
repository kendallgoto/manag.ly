package managly.backend.model;

import java.util.ArrayList;

public class Project extends Model {
	int projectId;
	String title;
	boolean archived;
	
	public Project(String title) {
		this.title = title;
		this.archived = false;		
	}
	public Project(String title, int projectId, boolean archived){
		this(title);
		this.archived = archived;
		this.projectId = projectId;
	}
	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	public int getId() {
		return projectId;
	}
	public void setId(int id) {
		this.projectId = id;
	}
}
