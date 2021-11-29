package managly.backend.http;

public class AddTeammateResponse {

	public int httpCode;
	public String response;
	public String error;
	
	
	public AddTeammateResponse(String rMessage, int code) {
		this.response = rMessage;
		this.httpCode = code;
	}
	
	public AddTeammateResponse (int statusCode, String errorMessage) {
		this.response = ""; // doesn't matter since error
		this.error = "" + statusCode;
		this.error = errorMessage;
	}
	
	// two other error responses?
	
	public String toString() {
		if (httpCode / 100 == 2) {  
			return "response(" + response + ")";
		} else {
			return "Errorresponse(" + httpCode + ", err=" + error + ")";
		}
	
	}
}
