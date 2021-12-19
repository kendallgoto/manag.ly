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
public class AddTaskTest extends LambdaTest {	
    @Test
    public void addTaskToProject() throws IOException {
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	
    	AddTaskHandler handler = new AddTaskHandler();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	ManaglyResponse response = handler.handleRequest(createTaskReq, createContext(""));
    	Assert.assertEquals("First Task successfully created", response.getClass(), TaskResponse.class);

    	TaskRequest createSecondReq = new Gson().fromJson( "{\"name\":\"My Less Cool Second Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	ManaglyResponse secondResp = handler.handleRequest(createSecondReq, createContext(""));
    	Assert.assertEquals("Second Task successfully created", secondResp.getClass(), TaskResponse.class);

    	TaskResponse task_first = (TaskResponse) response;
    	TaskResponse task_second = (TaskResponse) secondResp;
    	
    	Assert.assertEquals("First Task is labeled 1.", task_first.getTaskNumber(), "1.");
    	Assert.assertEquals("First Task has correct projectId", task_first.getProjectId(), projId);
    	Assert.assertNull("First Task has no parent", task_first.getParentId());

    	Assert.assertEquals("Second Task is labeled 2.", task_second.getTaskNumber(), "2.");
    	Assert.assertEquals("Second Task has correct projectId", task_second.getProjectId(), projId);
    	Assert.assertNull("Second Task has no parent", task_second.getParentId());
    }
        
    @Test
    public void addTaskToMissingProject() throws IOException {
    	AddTaskHandler handler = new AddTaskHandler();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"Some task that should fail\", \"projectId\": 99999}", TaskRequest.class);
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
	    	handler.handleRequest(createTaskReq, createContext(""));
		});
    }
    


}
