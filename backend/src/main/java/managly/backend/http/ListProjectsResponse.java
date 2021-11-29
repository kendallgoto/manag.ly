package managly.backend.http;

public class ListProjectsResponse {
	public int httpCode;
	public String response;
	public String error;
	
	
	public ListProjectsResponse(double rMessage, int code) {
		this.response = "" + rMessage;
		this.httpCode = code;
	}
	
	public ListProjectsResponse(String eMessage, int code) {
		this.response = eMessage;
		this.error = "" + code;
	}
	
	public String toString() {
		if (httpCode / 100 == 2) {  
			return "response(" + response + ")";
		} else {
			return "Errorresponse(" + httpCode + ", err=" + error + ")";
		}
	}


}
