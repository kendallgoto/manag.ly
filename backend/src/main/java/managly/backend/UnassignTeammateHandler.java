package managly.backend;

import java.sql.SQLException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TaskDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.AssignmentRequest;
import managly.backend.http.GenericErrorResponse;
import managly.backend.http.GenericSuccessResponse;

public class UnassignTeammateHandler implements RequestHandler<AssignmentRequest, ManaglyResponse>{

	@Override
	public ManaglyResponse handleRequest(AssignmentRequest req, Context context) {
		try {
			TeammateDocument existingTeammate = new TeammateDocument();
			if(existingTeammate.findById(req.getTeammateId())) {
				TaskDocument existingTask = new TaskDocument();
				if(existingTask.findById(req.getTaskId())) {
					existingTask.populateSubtasks();
					if(existingTask.getSubtasks().isEmpty()) {
						existingTask.populateTeammates();
						if(existingTask.getTeammates().contains(existingTeammate)) {
							ProjectDocument existingProj = new ProjectDocument();
							if(existingProj.findById(existingTeammate.getObject().getProjectId())) {
								if(existingTeammate.getObject().getProjectId() == existingTask.getObject().getProjectId()) {
									if(!existingProj.getObject().isArchived()) {
										if(existingTask.unassignTeammate(existingTeammate)) {
											return new GenericSuccessResponse(204, "Teammate successfully unassigned to task");
										}
										throw GenericErrorResponse.error(500, context, "Uncaught Error");
									}
									throw GenericErrorResponse.error(403, context, "Project is currently archived");
								}
								throw GenericErrorResponse.error(409, context, "Teammate and Task not in same Project.");
							}
							throw GenericErrorResponse.error(404, context, "Project not found.");
						}
						throw GenericErrorResponse.error(409, context, "Teammate is not already assigned to the task.");
					}
					throw GenericErrorResponse.error(400, context, "Task is a non-terminal task.");
				}
				throw GenericErrorResponse.error(404, context, "Task does not exist.");
			}
			throw GenericErrorResponse.error(404, context, "Teammate does not exist.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught SQL error.");
		}
	}

}
