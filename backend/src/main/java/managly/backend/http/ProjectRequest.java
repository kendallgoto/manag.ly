package managly.backend.http;

public class ProjectRequest {
	String title;
	
	public void setTitle(String title) { this.title = title; }
	public String getTitle() { return this.title; }
	
	ProjectRequest(String title) { this.title = title; }
	
	ProjectRequest() {}
	
	public String toString() {
		return "ProjectRequest("+title+")";
	}
}
