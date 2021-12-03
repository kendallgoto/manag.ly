package managly.backend;

import java.sql.SQLException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectRequest;
import managly.backend.http.ProjectResponse;
import managly.backend.db.ProjectDocument;
import managly.backend.http.GenericErrorResponse;


public class DeleteProjectHandler implements RequestHandler<ProjectRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(ProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling DeleteProjectHandler");
		logger.log(req.toString());
		
		ProjectDocument deletedProj = new ProjectDocument();
		try {
			if(deletedProj.save()) {
				if(deletedProj.save()) {
					logger.log("Project found "+deletedProj.getObject().getId());
					return new ProjectResponse(deletedProj);
			} else {
			throw GenericErrorResponse.error(404, context, "Project does not exist");
		}
		}catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "some SQL error occured");
		}
	

		return null;
	}
}
