package managly.backend;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TeammateRequest;
import managly.backend.http.GenericErrorResponse;


public class RemoveTeammateHandler implements RequestHandler<TeammateRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(TeammateRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling RemoveTeammateHandler");
		logger.log(req.toString());

		return null;
	}
}