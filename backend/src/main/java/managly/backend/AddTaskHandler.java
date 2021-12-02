package managly.backend;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TaskRequest;
import managly.backend.http.GenericErrorResponse;


public class AddTaskHandler implements RequestHandler<TaskRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(TaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling AddTaskHandler");
		logger.log(req.toString());

		return null;
	}
}
