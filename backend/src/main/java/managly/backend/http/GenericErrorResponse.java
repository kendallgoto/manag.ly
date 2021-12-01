package managly.backend.http;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericErrorResponse extends RuntimeException implements ManaglyResponse {
	
	private GenericErrorResponse (String error) {
		super(error);
	}
	
	//https://aws.amazon.com/blogs/compute/error-handling-patterns-in-amazon-api-gateway-and-aws-lambda/
	public static RuntimeException error(int code, Context context, String error) {
        Map<String, Object> errorPayload = new HashMap();
        errorPayload.put("httpStatus", code);
        errorPayload.put("requestId", context.getAwsRequestId());
        errorPayload.put("message", error);
        try {
	        String message = new ObjectMapper().writeValueAsString(errorPayload);
	        return new GenericErrorResponse(message);
        } catch(Exception e) {
        	return new GenericErrorResponse(error); // resort to default error
        }
	}
}
