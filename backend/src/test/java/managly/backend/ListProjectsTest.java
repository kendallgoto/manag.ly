package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ListProjectsTest extends LambdaTest {
	
    @Test
    public void testGetList() throws IOException {

    	CreateProjectHandler handler = new CreateProjectHandler();
    	CreateProjectRequest req = new Gson().fromJson( "test", CreateProjectRequest.class);
    	ManaglyResponse response = handler.handleRequest(req, createContext(""));
    	Assert.assertEquals(response.toString(),new GenericErrorResponse(200, "OK!").toString());
    }

}
