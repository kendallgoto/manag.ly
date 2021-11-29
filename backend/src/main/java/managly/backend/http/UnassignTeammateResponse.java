package managly.backend.http;

public class UnassignTeammateResponse {
	public int httpCode;
	public String response;
	public String error;
	
	
	public UnassignTeammateResponse(String rMessage, int code) {
		this.response = rMessage;
		this.httpCode = code;
	}
	
	public UnassignTeammateResponse (int statusCode, String errorMessage) {
		this.response = ""; // doesn't matter since error
		this.error = "" + statusCode;
		this.error = errorMessage;
	}
	
	// 2 more error responses?
	
	public String toString() {
		if (httpCode / 100 == 2) {  
			return "response(" + response + ")";
		} else {
			return "Errorresponse(" + httpCode + ", err=" + error + ")";
		}
	
	}

}
