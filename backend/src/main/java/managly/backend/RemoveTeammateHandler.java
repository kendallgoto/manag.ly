package managly.backend;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TeammateRequest;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.GenericErrorResponse;
import managly.backend.http.GenericSuccessResponse;


public class RemoveTeammateHandler implements RequestHandler<TeammateRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(TeammateRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling RemoveTeammateHandler");
		logger.log(req.toString());
		
		TeammateDocument deleteTeammate = new TeammateDocument(req.getName(), req.getProjectID());
		ProjectDocument existingProj = new ProjectDocument();
		try {
			if(existingProj.findById(req.getProjectID())) {
				if(!existingProj.getObject().isArchived()) {
					if(deleteTeammate.delete()) {
						return new GenericSuccessResponse(204, "Teammate is successfully deleted.");
					}else {
						throw GenericErrorResponse.error(404, context, "Teammate does not exist.");
					}
				} else {
					throw GenericErrorResponse.error(403, context, "Project is archived.");
				}
			} else {
				throw GenericErrorResponse.error(410, context, "Project not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught SQL error.");
		}
	}
}
