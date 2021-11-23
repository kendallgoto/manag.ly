package managly.backend.model;

import java.util.ArrayList;

public class Teammate {
	int teammateId;
	String name;
	int projectId;
	ArrayList<Task> tasks;
	
	Teammate(String name){
		this.name = name;
		this.tasks = new ArrayList<Task>();
	}
	Teammate(int tId, String name, int pId, ArrayList<Task> tasks){
		this(name);
		this.teammateId = tId;
		this.projectId = pId;
		this.tasks = (tasks == null) ? this.tasks : tasks;
	}
}
