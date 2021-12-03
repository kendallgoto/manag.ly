package managly.backend;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import managly.backend.db.ProjectDocument;
import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class DeleteProjectTest extends LambdaTest {	
    @Test
    public void deleteProject() throws IOException, SQLException {
    	//Create Project
    	//Create Task + add to Project
    	//Create subtask + add to Project
    	//Create Teammate + add to Project
    	//Create TaskAssignment + add to Project
    	//Delete Project
    	//Verify Teammate, Task, Subtask, TaskAssignment are deleted.
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	ProjectDocument deletedProject = new ProjectDocument("Test Project");
    	DeleteProjectHandler deleteHandler = new DeleteProjectHandler();
    	AddTeammateHandler teamHandler = new AddTeammateHandler();
    	TeammateRequest teamReq = new Gson().fromJson( "{\"name\":\"test name\", \"projectId\":"+newProj.getId() +"}", ProjectRequest.class);
    	ManaglyResponse teamResponse = teamHandler.handleRequest(teamReq, createContext(""));
    	AddTaskHandler taskHandler = new AddTaskHandler();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	ManaglyResponse taskResponse = taskHandler.handleRequest(createTaskReq, createContext(""));
    	deletedProject.delete();
    	ProjectRequest findReq = new Gson().fromJson( "{\"projectId\":99}", ProjectRequest.class);

    	
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
	    	deleteHandler.handleRequest(findReq, createContext(""));
		});
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
	    	teamHandler.handleRequest(teamReq, createContext(""));
		});
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
	    	taskHandler.handleRequest(createTaskReq, createContext(""));
		});
    	
    }
    
    @Test
    public void deleteMissingProject() throws IOException {
    	//Call delete + fail
    	DeleteProjectHandler findProject = new DeleteProjectHandler();
    	ProjectRequest findReq = new Gson().fromJson( "{\"projectId\":99}", ProjectRequest.class);
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
	    	findProject.handleRequest(findReq, createContext(""));
		});
    }
}
