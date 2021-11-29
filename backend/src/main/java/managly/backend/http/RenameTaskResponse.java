package managly.backend.http;

public class RenameTaskResponse {
	
	public int httpCode;
	public String response;
	public String error;
	
	
	public RenameTaskResponse(String rMessage, int code) {
		this.response = rMessage;
		this.httpCode = code;
	}
	
	public RenameTaskResponse (int statusCode, String errorMessage) {
		this.response = ""; // doesn't matter since error
		this.error = "" + statusCode;
		this.error = errorMessage;
	}
	
	// 1 more error response?
	
	public String toString() {
		if (httpCode / 100 == 2) {  
			return "response(" + response + ")";
		} else {
			return "Errorresponse(" + httpCode + ", err=" + error + ")";
		}
	
	}

}
