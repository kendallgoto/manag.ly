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
import managly.backend.http.ListProjectsRequest;


public class ListProjectsHandler implements RequestHandler<ListProjectsRequest, ManaglyResponse> {
	
	public LambdaLogger logger;

	@Override
	public ManaglyResponse handleRequest(ListProjectsRequest req, Context context) {
		logger = context.getLogger();
		logger.log("Handling ListProjectsRequest");
		logger.log(req.toString());
		
		return null;
	}
}
