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

public class MarkTaskHandler implements RequestHandler<TaskRequest, ManaglyResponse> {
	
	public LambdaLogger logger;
	
	@Override
	public ManaglyResponse handleRequest(TaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling MarkTaskHandler");
		logger.log(req.toString());
		
		TaskDocument existingTask = new TaskDocument();
		ProjectDocument existingProj = new ProjectDocument();
		try {
			if(existingTask.findById(req.getTaskId())) { // task exists
				existingProj.findById(existingTask.getObject().getProjectId());
				if(!existingProj.getObject().isArchived()) { // project is not archived
					if(existingTask.getObject().isCompleted() != req.getMarkTask()) { // states differ
						existingTask.populateSubtasks();
						if(existingTask.getSubtasks().isEmpty()) { // task is terminal
							existingTask.getObject().setCompleted(req.getMarkTask());
							if(existingTask.save()) {
								return new GenericSuccessResponse(204, "Task mark is updated.");
							}
							throw GenericErrorResponse.error(500, context, "Uncaught saving error");
						}
						throw GenericErrorResponse.error(400, context, "Task to mark is not a terminal task.");
					}
					throw GenericErrorResponse.error(409, context, "State is already as marked");
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