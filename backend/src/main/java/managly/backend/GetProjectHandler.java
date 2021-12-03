package managly.backend;

import java.sql.SQLException;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.db.ProjectDocument;
import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectRequest;
import managly.backend.http.ProjectResponse;
import managly.backend.http.GenericErrorResponse;


public class GetProjectHandler implements RequestHandler<ProjectRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(ProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling GetProjectHandler");
		logger.log(req.toString());

		ProjectDocument newProj = new ProjectDocument();
		try {
			if(newProj.findById(req.getProjectId())) {
				newProj.populateTasks();
				newProj.populateTeammates();
				return new ProjectResponse(newProj);
			} else {
				throw GenericErrorResponse.error(404, context, "Project not found");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "some SQL error");
		}
	}
}
