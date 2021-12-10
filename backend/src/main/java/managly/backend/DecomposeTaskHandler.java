package managly.backend;

import java.sql.SQLException;
import java.util.List;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.db.ProjectDocument;
import managly.backend.db.TaskDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.ManaglyResponse;
import managly.backend.http.TaskResponse;
import managly.backend.http.DecomposeRequest;
import managly.backend.http.GenericErrorResponse;


public class DecomposeTaskHandler implements RequestHandler<DecomposeRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(DecomposeRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling DecomposeTaskHandler");
		logger.log(req.toString());

		
		TaskDocument baseTask = new TaskDocument();
		try {
			if(baseTask.findById(req.getTaskId())) {
				baseTask.populateSubtasks();
				if(baseTask.getSubtasks().isEmpty()) {
					ProjectDocument parentProject = new ProjectDocument();
					if(parentProject.findById(baseTask.getObject().getProjectId())) {
						if(!parentProject.getObject().isArchived()) {
							
							//create new tasks for each child ...
							for(int i = 0; i < req.getSubtasks().length; i++) {
								TaskDocument newTask = new TaskDocument(
										baseTask.getObject().getTaskNumber() + (i+1) + ".",
										req.getSubtasks()[i].getName(),
										baseTask.getObject().getProjectId(),
										baseTask.getObject().getId()
								);
								if(newTask.save()) {
									logger.log("created new subtask with ID "+newTask.getObject().getId());
								} else {
									logger.log("FAILED to create subtask -- fatal uncaught error!");
									//throw GenericErrorResponse.error(500, context, "Uncaught saving error");
								}
							}
							baseTask.populateSubtasks();
							baseTask.getTeammates();
							List<TeammateDocument> parentAssigned = baseTask.getTeammates();
							if(parentAssigned != null && parentAssigned.size() > 0) {
								for(int i = 0; i < baseTask.getSubtasks().size(); i++) {
									TaskDocument thisSubtask = baseTask.getSubtasks().get(i);
									thisSubtask.assignTeammate(parentAssigned.get(i % parentAssigned.size()));
								}
								for(TeammateDocument origTeammate : parentAssigned) { // non-terminal tasks don't have assignments
									baseTask.unassignTeammate(origTeammate);
								}
							}
							
							baseTask.getObject().setCompleted(false); // non-terminal tasks don't have completed flag
							baseTask.save();
							
							baseTask.populateSubtasks();
							baseTask.getTeammates();
							return new TaskResponse(baseTask);							
						}
						throw GenericErrorResponse.error(403, context, "Project is archived");
					}
					throw GenericErrorResponse.error(500, context, "Task's parent project does not exist.");
				}
				throw GenericErrorResponse.error(400, context, "Task is already decomposed");
			}
			throw GenericErrorResponse.error(404, context, "Task is not found");
		} catch (SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught SQL error");
		}
	}
}
