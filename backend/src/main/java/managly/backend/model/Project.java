package managly.backend.model;

import java.util.ArrayList;

public class Project {
	int projectId;
	String title;
	boolean archived;
	ArrayList<Task> tasks;
	ArrayList<Teammate> teammates;
	
	Project(String title){
		this.title = title;
		this.archived = false;
		this.tasks = new ArrayList<Task>();
		this.teammates = new ArrayList<Teammate>();
		
	}
	Project(String title, int projectId, boolean archived, ArrayList<Task> tasks, ArrayList<Teammate> teammates){
		this(title);
		this.archived = archived;
		this.projectId = projectId;
		this.tasks = (tasks == null) ? this.tasks : tasks;
		this.teammates = (teammates == null) ? this.teammates : teammates;
	}
}
