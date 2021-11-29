package managly.backend.http;

public class DeleteProjectResponse {

	public int httpCode;
	public String response;
	public String error;
	
	
	public DeleteProjectResponse(String rMessage, int code) {
		this.response = rMessage;
		this.httpCode = code;
	}
	
	public DeleteProjectResponse (int statusCode, String errorMessage) {
		this.response = ""; // doesn't matter since error
		this.error = "" + statusCode;
		this.error = errorMessage;
	}
	
	
	public String toString() {
		if (httpCode / 100 == 2) {  
			return "response(" + response + ")";
		} else {
			return "Errorresponse(" + httpCode + ", err=" + error + ")";
		}
	
	}
}
