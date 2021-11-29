package managly.backend.http;

public class ArchiveProjectResponse {

	public int httpCode;
	public String response;
	public String error;
	
	
	public ArchiveProjectResponse(String rMessage, int code) {
		this.response = rMessage;
		this.httpCode = code;
	}
	
	public ArchiveProjectResponse (int statusCode, String errorMessage) {
		this.response = ""; // doesn't matter since error
		this.error = "" + statusCode;
		this.error = errorMessage;
	}
	
	//one more response?

	
	public String toString() {
		if (httpCode / 100 == 2) {  
			return "response(" + response + ")";
		} else {
			return "Errorresponse(" + httpCode + ", err=" + error + ")";
		}
	
	}
}