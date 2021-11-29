package managly.backend.http;

public class GetProjectResponse {

	public int httpCode;
	public String response;
	public String error;
	
	
	public GetProjectResponse(String rMessage, int code) {
		this.response = rMessage;
		this.httpCode = code;
	}
	
	public GetProjectResponse (int statusCode, String errorMessage) {
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
