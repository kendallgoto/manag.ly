package managly.backend;

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
import managly.backend.http.ManaglyResponse;
import managly.backend.http.ProjectResponse;
import managly.backend.http.GenericErrorResponse;
import managly.backend.http.GetProjectRequest;


public class GetProjectHandler implements RequestHandler<GetProjectRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(GetProjectRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling GetProjectHandler");
		logger.log(req.toString());

		ProjectDocument newProj = new ProjectDocument();
		try {
			if(newProj.findById(1)) {
				return new ProjectResponse(newProj.getObject());
			} else {
				throw GenericErrorResponse.error(404, context, "Project not found");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			throw GenericErrorResponse.error(500, context, "some SQL error");
		}
	}
}
