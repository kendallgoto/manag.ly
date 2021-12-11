package managly.backend;

import java.sql.SQLException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TaskRequest;
import managly.backend.http.TaskResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TaskDocument;
import managly.backend.http.GenericErrorResponse;


public class AddTaskHandler implements RequestHandler<TaskRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(TaskRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling AddTaskHandler");
		logger.log(req.toString());
		
		//Find project to validate ...
		ProjectDocument existingProj = new ProjectDocument();
		try {
			if(existingProj.findById(req.getProjectId())) {
				if(!existingProj.getObject().isArchived()) {
					//Create task ...
					TaskDocument newTask;
					if(req.getTaskParent() != null) {
						logger.log("Adding subtask ... ");
						newTask = new TaskDocument(
								TaskDocument.getNextPath(existingProj.getObject().getId(), req.getTaskParent()),
								req.getName(),
								existingProj.getObject().getId(),
								req.getTaskParent()
							);
					} else {
						newTask = new TaskDocument(
							TaskDocument.getNextPath(existingProj.getObject().getId(), null),
							req.getName(),
							existingProj.getObject().getId(),
							req.getTaskParent()
						);
					}
					if(newTask.save()) {
						logger.log("created new task with ID "+newTask.getObject().getId());
						return new TaskResponse(newTask);
					}
					throw GenericErrorResponse.error(500, context, "Uncaught saving error");
				}
				throw GenericErrorResponse.error(403, context, "Project is archived.");
			}
			throw GenericErrorResponse.error(404, context, "Project not found");
		} catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught MySQL error");
		}
	}
}
