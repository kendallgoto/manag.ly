package managly.backend;

import java.util.List;

import com.amazonaws.services.lambda.runtime.*;

import managly.backend.db.ProjectDocument;
import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectResponseArray;


public class ListProjectsHandler implements RequestHandler<Object, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(Object empyRequest, Context context) {
		logger = context.getLogger();
		logger.log("Handling ListProjectsRequest");
		
		List<ProjectDocument> allProjects = ProjectDocument.gather();
		
		return new ProjectResponseArray(allProjects);
	}
}
