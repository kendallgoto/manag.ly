package managly.backend;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectRequest;
import managly.backend.http.TeammateRequest;
import managly.backend.http.TeammateResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.GenericErrorResponse;
import managly.backend.http.GenericSuccessResponse;

public class ArchiveProjectHandler implements RequestHandler<ProjectRequest, ManaglyResponse> {
	
	public LambdaLogger logger;
	
	@Override
	public ManaglyResponse handleRequest(ProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling ArchiveProjectHandler");
		logger.log(req.toString());
		
		ProjectDocument existingProj = new ProjectDocument();
		try {
			if(existingProj.findById(req.getProjectId())) {
				if(!existingProj.getObject().isArchived()) {
					existingProj.getObject().setArchived(true);
					if(existingProj.save()) {
						return new GenericSuccessResponse(204, "Project updated successfully");
					}
					throw GenericErrorResponse.error(500, context, "Uncaught saving error");
				}
				throw GenericErrorResponse.error(403, context, "Project is already archived.");
			}
			throw GenericErrorResponse.error(404, context, "Project not found");
		} catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught MySQL error");
		}
	}
}