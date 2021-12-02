package managly.backend;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TeammateRequest;
import managly.backend.http.GenericErrorResponse;
//William tryna figure out how to write this.

public class AddTeammateHandler implements RequestHandler<TeammateRequest, ManaglyResponse> {
	
	public LambdaLogger logger;
	
	@Override
	public ManaglyResponse handleRequest(TeammateRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling AddTeammateHandler");
		logger.log(req.toString());
		

		ProjectDocument existingProj = new ProjectDocument();
		try {
			if(existingProj.findById(req.getProjectId())) {
				if(!existingProj.getObject().isArchived()) {
					TeammateDocument newTeammate = new TeammateDocument(req.getName(), req.projectID);
					if(newProj.save()) {
						logger.log("New project saved with ID "+newTeammate.getObject().getId());
						return new ProjectResponse(newTeammate);
					} else {
						throw GenericErrorResponse.error(500, context, "Uncaught saving error");
					}	
				} else {
					throw GenericErrorResponse.error(403, context, "Project is archived.");
				}
			}else {
				throw GenericErrorResponse.error(404, context, "Project does not exist");
			}
		}catch(SQLException e) {
			if(e.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
				e.printStackTrace();
				throw GenericErrorResponse.error(409, context, "Teammate with this name is already in the project.");
			} 
			else {
				e.printStackTrace();
				throw GenericErrorResponse.error(500, context, "Uncaught SQL error");
			}
		}
		return null;
	}
}
