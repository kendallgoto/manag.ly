package managly.backend.http;

public class GenericSuccessResponse implements ManaglyResponse {	
	public int status;
	public String statusMessage;
	public GenericSuccessResponse(int status, String statusMessage) { 
		this.status = status;
		this.statusMessage = statusMessage;
	}
	
	public String toString() {
		return "GenericSuccessResponse("+this.status+")";
	}

}
