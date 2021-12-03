package managly.backend;

import java.sql.SQLException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectRequest;
import managly.backend.http.ProjectResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.GenericErrorResponse;
import managly.backend.http.GenericSuccessResponse;


public class DeleteProjectHandler implements RequestHandler<ProjectRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(ProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling DeleteProjectHandler");
		logger.log(req.toString());
		
		ProjectDocument existingProj = new ProjectDocument();
		try {
			if(existingProj.findById(req.getProjectId())) {
				
				//Deep populate
				if(existingProj.delete()) {
					return new GenericSuccessResponse(204, "Project is successfully deleted.");
				} else {
					throw GenericErrorResponse.error(500, context, "Uncaught deletion error");
				}
			} else {
				throw GenericErrorResponse.error(404, context, "Project not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught SQL error.");
		}
	}
}
