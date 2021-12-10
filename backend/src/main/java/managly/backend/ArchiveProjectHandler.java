package managly.backend;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.TeammateRequest;
import managly.backend.http.TeammateResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TeammateDocument;
import managly.backend.http.GenericErrorResponse;

public class ArchiveTaskHandler implements RequestHandler<TaskRequest, ManaglyResponse> {
	
	public LambdaLogger logger;
	
	@Override
	public ManaglyResponse handleRequest(TeammateRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling AddTeammateHandler");
		logger.log(req.toString());
		
		ProjectDocument existingProj = new ProjectDocument();

		try {
			if(!existingProj.getObject().isArchived()) {
				existingProj.getObject().setArchived(true);
				if(existingProj.save()) {
					logger.log("Project is successfully marked archived.");
					return new ProjectResponse(existingTask);
				} else {
					throw GenericErrorResponse.error(500, context, "Uncaught saving error");
				}
			} else {
				throw GenericErrorResponse.error(403, context, "Project is already archived.");
			}
		 else {
			throw GenericErrorResponse.error(404, context, "Project not found");
		}
		} catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "Uncaught MySQL error");
		}
	}
}