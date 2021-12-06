package managly.backend;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TeammateRequest;
import managly.backend.http.TeammateResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.GenericErrorResponse;

public class RenameTaskHandler implements RequestHandler<TaskRequest, ManaglyResponse> {
	
	public LambdaLogger logger;
	
	@Override
	public ManaglyResponse handleRequest(TaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling AddTeammateHandler");
		logger.log(req.toString());
		
		TaskDocument existingTask = new TaskDocument();

		
		
		try {
			if(existingTask.findById(req.getTaskId())) {
				if(!existingProj.getObject().isArchived()) {
					existingTask.getObject().setName(req.getName());
					if(existingTask.save()) {
						logger.log("Task is successfully renamed.");
						return new TaskResponse(existingTask);
					} else {
						throw GenericErrorResponse.error(500, context, "Uncaught saving error");
					}
				} else {
					throw GenericErrorResponse.error(403, context, "Project is archived.");
				}
			} else {
				throw GenericErrorResponse.error(404, context, "Project not found");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught MySQL error");
		}
	}
	
}