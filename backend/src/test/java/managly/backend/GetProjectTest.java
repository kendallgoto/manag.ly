package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetProjectTest extends LambdaTest {	
    @Test
    public void testExistingProject() throws IOException {
    	CreateProjectHandler handler = new CreateProjectHandler();
    	ProjectRequest req = new Gson().fromJson( "{\"title\":\"Project to Find\"}", ProjectRequest.class);
    	ManaglyResponse response = handler.handleRequest(req, createContext(""));
    	Assert.assertEquals("Created Project to Find", response.getClass(), ProjectResponse.class);
    	ProjectResponse prResp = (ProjectResponse) response;
    	
    	GetProjectHandler findProject = new GetProjectHandler();
    	ProjectRequest findReq = new Gson().fromJson( "{\"projectId\":"+prResp.getId()+"}", ProjectRequest.class);
    	ManaglyResponse findedResponse = findProject.handleRequest(findReq, createContext(""));
    	Assert.assertEquals("Created Project to Find", findedResponse.getClass(), ProjectResponse.class);
    	ProjectResponse prFinded = (ProjectResponse) findedResponse;
    	Assert.assertEquals("Found project is the same as original", prResp.toString(), prFinded.toString());
    	Assert.assertArrayEquals("Found project has same tasks as original", prResp.tasks, prFinded.tasks);
    	Assert.assertArrayEquals("Found project has same teammates as original", prResp.teammates, prFinded.teammates);
    	
    	//TODO: Add task + teammates, then re-find and test equality
    }
    
    @Test
    public void testMissingProject() throws IOException {
    	//Find a nonexisting project with GetProjectHandler
    	GetProjectHandler findProject = new GetProjectHandler();
    	ProjectRequest findReq = new Gson().fromJson( "{\"projectId\":99}", ProjectRequest.class);
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
	    	findProject.handleRequest(findReq, createContext(""));
		});
    }
}
