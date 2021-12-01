package managly.backend.http;

public class DecomposeRequest {
	String[] subTasks;
	
	public DecomposeRequest(String[] subTasks) {
		this.subTasks = subTasks;
	}
	
	public DecomposeRequest() {}
	
	public String toString() {
		return "Decompose Task("+subTasks+")";
	}

}