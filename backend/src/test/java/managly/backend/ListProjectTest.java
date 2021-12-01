package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ListProjectTest extends LambdaTest {	
	
	@Test
	public void testEmptyList() throws IOException {
		ListProjectsHandler handler = new ListProjectsHandler();
		ManaglyResponse responses = handler.handleRequest(null, createContext(""));
		Assert.assertEquals("No Projects to List", responses.toString(), "ProjectResponseArray[0]");
	}
	
	@Test
    public void testList() throws IOException {
    	CreateProjectHandler createHandler = new CreateProjectHandler();
    	ProjectRequest createProjectOne = new Gson().fromJson( "{\"title\":\"project one\"}", ProjectRequest.class);
    	createHandler.handleRequest(createProjectOne, createContext(""));

    	ProjectRequest createProjectTwo = new Gson().fromJson( "{\"title\":\"project two\"}", ProjectRequest.class);
    	createHandler.handleRequest(createProjectTwo, createContext(""));
    	
		ListProjectsHandler listHandler = new ListProjectsHandler();
		ManaglyResponse responses = listHandler.handleRequest(null, createContext(""));
		Assert.assertEquals("List of All Projects contains 2", responses.toString(), "ProjectResponseArray[2]");
		
		ProjectResponse[] castedResponses = ((ProjectResponseArray)responses).projects;
		Assert.assertEquals(castedResponses[0].getTitle(), "project one");
		Assert.assertEquals(castedResponses[1].getTitle(), "project two");
    }

	
    
}
