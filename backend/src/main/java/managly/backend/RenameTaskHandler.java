package managly.backend;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TaskRequest;
import managly.backend.http.TaskResponse;
import managly.backend.http.TeammateRequest;
import managly.backend.http.TeammateResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TaskDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.GenericErrorResponse;
import managly.backend.http.GenericSuccessResponse;

public class RenameTaskHandler implements RequestHandler<TaskRequest, ManaglyResponse> {
	
	public LambdaLogger logger;
	
	@Override
	public ManaglyResponse handleRequest(TaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling RenameTaskHandler");
		logger.log(req.toString());
		
		TaskDocument existingTask = new TaskDocument();
		ProjectDocument existingProj = new ProjectDocument();
		try {
			if(existingTask.findById(req.getTaskId())) { // task exists
				existingProj.findById(existingTask.getObject().getProjectId());
				if(!existingProj.getObject().isArchived()) { // project is not archived
					existingTask.getObject().setName(req.getName());
					if(existingTask.save()) {
						return new TaskResponse(existingTask);
					}
					throw GenericErrorResponse.error(500, context, "Uncaught saving error");
				}
				throw GenericErrorResponse.error(403, context, "Project is archived.");
			}
			throw GenericErrorResponse.error(404, context, "Task not found");
		} catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught MySQL error");
		}
	}
	
}