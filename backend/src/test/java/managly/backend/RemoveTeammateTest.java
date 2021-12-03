package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class RemoveTeammateTest extends LambdaTest {	
    @Test
    public void deleteTeammate() throws IOException {
    	//TODO(grumski): finish this function when we finish the code for adding and assigning tasks
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();

    	//Add Teammate
    	TeammateRequest firstTeammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	new AddTeammateHandler().handleRequest(firstTeammateCreate, createContext(""));
    	
    	//Add Second Teammate
    	TeammateRequest secondTeammateCreate = new Gson().fromJson( "{\"name\":\"John Appleseed\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	new AddTeammateHandler().handleRequest(secondTeammateCreate, createContext(""));

    	RemoveTeammateHandler removeTeammateHandler = new RemoveTeammateHandler();
    	TeammateRequest deleteFirstTeammate = new Gson().fromJson( "{\"teammateId\":1}", TeammateRequest.class);
    	ManaglyResponse response = removeTeammateHandler.handleRequest(deleteFirstTeammate, createContext(""));
    	Assert.assertEquals("Teammate successfully deleted", response.getClass(), GenericSuccessResponse.class);
    }
    
    @Test
    public void deleteMissingTeammate() throws IOException {
    	CreateProjectHandler projectHandler = new CreateProjectHandler();
    	ProjectRequest projectReq = new Gson().fromJson( "{\"title\":\"my project test\"}", ProjectRequest.class);
    	ManaglyResponse response = projectHandler.handleRequest(projectReq, createContext(""));
    	int projectID = ((ProjectResponse)response).getId();
    	TeammateRequest teammate = new TeammateRequest("nick", projectID);
    	RemoveTeammateHandler removeTeammateHandler = new RemoveTeammateHandler();
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		removeTeammateHandler.handleRequest(teammate, createContext(""));
		});
    }
    
    @Test
    public void deleteMissingTeammateFromMissingProject() throws IOException {
    	TeammateRequest teammate = new TeammateRequest("nick", 0);
    	RemoveTeammateHandler removeTeammateHandler = new RemoveTeammateHandler();
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		removeTeammateHandler.handleRequest(teammate, createContext(""));
		});
    	
    }
    
    @Test
    public void deleteMissingTeammateFromLockedProject() throws IOException {
    	CreateProjectHandler projectHandler = new CreateProjectHandler();
    	ProjectRequest projectReq = new Gson().fromJson( "{\"title\":\"my project test\"}", ProjectRequest.class);
    	ManaglyResponse response = projectHandler.handleRequest(projectReq, createContext(""));
    	int projectID = ((ProjectResponse)response).getId();
    	//lock project
    	TeammateRequest teammate = new TeammateRequest("nick", projectID);
    	RemoveTeammateHandler removeTeammateHandler = new RemoveTeammateHandler();
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		removeTeammateHandler.handleRequest(teammate, createContext(""));
		});
    }
}
