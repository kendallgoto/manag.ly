package managly.backend.http;

public class CreateProjectResponse {

	public int httpCode;
	public String response;
	public String error;
	
	
	public CreateProjectResponse(String rMessage, int code) {
		this.response = rMessage;
		this.httpCode = code;
	}
	
	
	public String toString() {
		if (httpCode / 100 == 2) {  
			return "response(" + response + ")";
		} else {
			return "Errorresponse(" + httpCode + ", err=" + error + ")";
		}
	}
	
}
