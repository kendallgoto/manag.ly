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
    	CreateProjectHandler projectHandler = new CreateProjectHandler();
    	ProjectRequest projectReq = new Gson().fromJson( "{\"title\":\"my project test\"}", ProjectRequest.class);
    	ManaglyResponse response = projectHandler.handleRequest(projectReq, createContext(""));
    	int projectID = ((ProjectResponse)response).getId();
    	//Create Task + add to Project
    	//Create subtask + add to Project
    	AddTeammateHandler addTeammateHandler = new AddTeammateHandler();
    	TeammateRequest teammate = new TeammateRequest("nick", projectID);
    	addTeammateHandler.handleRequest(teammate, createContext(""));
    	//Create TaskAssignment + add to Project
    	RemoveTeammateHandler removeTeammateHandler = new RemoveTeammateHandler();
    	response = removeTeammateHandler.handleRequest(teammate, createContext(""));
    	//Verify TaskAssignment are deleted.
    	Assert.assertEquals(new GenericSuccessResponse(204, "Teammate is successfully deleted.").toString(), response.toString());
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
