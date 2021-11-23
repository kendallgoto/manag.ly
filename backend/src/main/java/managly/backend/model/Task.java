package managly.backend.model;

import java.util.ArrayList;

public class Task {
	int taskId;
	String taskNumber;
	int parentId;
	boolean completed;
	String name;
	int projectId;
	ArrayList<Task> children;
	ArrayList<Teammate> assignees;
	
	Task(String taskNumber, String name, int projectId){
		this.taskNumber = taskNumber;
		this.name = name;
		this.completed = false;
		this.projectId = projectId;
		this.children = new ArrayList<Task>();
		this.assignees = new ArrayList<Teammate>();
	}
	Task(String taskNumber, String name, int taskId, boolean completed, int projectId, ArrayList<Task> children, ArrayList<Teammate> assignees){
		this(taskNumber, name, projectId);
		this.children = (children == null) ? this.children : children;
		this.completed = completed;
		this.taskId = taskId;
		this.assignees = (assignees == null) ? this.assignees : assignees;
	}
	Task(String taskNumber, String name, int taskId, boolean completed, int projectId, ArrayList<Task> children, ArrayList<Teammate> assignees, int parentId){
		this(taskNumber, name, taskId, completed, projectId, children, assignees);
		this.parentId = parentId;
	}
}
