package managly.backend;

import java.io.UncheckedIOException;
import java.sql.SQLException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import managly.backend.db.ProjectDocument;
import managly.backend.http.CreateProjectRequest;
import managly.backend.http.CreateProjectResponse;
import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectResponse;
import managly.backend.http.GenericErrorResponse;


public class CreateProjectHandler implements RequestHandler<CreateProjectRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(CreateProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling CreateProjectRequest");
		logger.log(req.toString());

		ProjectDocument newProj = new ProjectDocument(req.getProject());
		try {
			if(newProj.save()) {
				logger.log("New project saved with ID "+newProj.getObject().getId());
				return new ProjectResponse(newProj.getObject());
			} else {
				throw GenericErrorResponse.error(500, context, "Uncaught saving error");
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(409, context, "Project with this name already exists");
		}
	}
}
