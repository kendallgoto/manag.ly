package managly.backend;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.db.ProjectDocument;
import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectRequest;
import managly.backend.http.ProjectResponse;
import managly.backend.http.GenericErrorResponse;


public class CreateProjectHandler implements RequestHandler<ProjectRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(ProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling CreateProjectRequest");
		logger.log(req.toString());

		ProjectDocument newProj = new ProjectDocument(req.getTitle());
		try {
			if(newProj.save()) {
				logger.log("New project saved with ID "+newProj.getObject().getId());
				return new ProjectResponse(newProj.getObject());
			} else {
				throw GenericErrorResponse.error(500, context, "Uncaught saving error");
			}	
		} catch (SQLException e) {
			if(e.getClass().equals(SQLIntegrityConstraintViolationException.class))
				throw GenericErrorResponse.error(409, context, "Project with this name already exists");
			else {
				e.printStackTrace();
				throw GenericErrorResponse.error(500, context, "Uncaught SQL error");
			}
		}
	}
}
