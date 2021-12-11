package managly.backend;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class DeleteProjectTest extends LambdaTest {	
    @Test
    public void deleteProject() throws IOException, SQLException {
    	//Delete Project
    	//Verify Teammate, Task, Subtask, TaskAssignment are deleted.
    	//Create project
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();

    	//Add Task (TODO: add Subtask)
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	new AddTaskHandler().handleRequest(createTaskReq, createContext(""));

    	//Add Teammate (TODO: add Assignment
    	TeammateRequest createTeammateRequest = new Gson().fromJson( "{\"name\":\"test name\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	new AddTeammateHandler().handleRequest(createTeammateRequest, createContext(""));

    	
    	ProjectRequest deleteProjectRequest = new Gson().fromJson( "{\"projectId\":"+projId +"}", ProjectRequest.class);
    	ManaglyResponse resp = new DeleteProjectHandler().handleRequest(deleteProjectRequest, createContext(""));
    	
    	//TODO: this should delete all attached entities and be validated! (though the foreign key constraints will help with this)
    	Assert.assertEquals("Project successfully deleted", resp.getClass(), GenericSuccessResponse.class);
    }
    
    @Test
    public void deleteMissingProject() throws IOException {
    	ProjectRequest deleteReq = new Gson().fromJson( "{\"projectId\":99}", ProjectRequest.class);
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
			new DeleteProjectHandler().handleRequest(deleteReq, createContext(""));
		});
    }
}
