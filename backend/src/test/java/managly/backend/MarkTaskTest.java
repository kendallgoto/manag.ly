package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class MarkTaskTest extends LambdaTest {	
    @Test
    public void markTaskCompleted() throws IOException {
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	
    	AddTaskHandler handler = new AddTaskHandler();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	ManaglyResponse response = handler.handleRequest(createTaskReq, createContext(""));
    	Assert.assertEquals("First Task successfully created", response.getClass(), TaskResponse.class);

    	TaskRequest createSecondReq = new Gson().fromJson( "{\"name\":\"My Less Cool Second Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	ManaglyResponse secondResp = handler.handleRequest(createSecondReq, createContext(""));
    	Assert.assertEquals("Second Task successfully created", secondResp.getClass(), TaskResponse.class);
    	TaskResponse resp = (TaskResponse) secondResp;
    	
    	TaskRequest markTaskRequest = new Gson().fromJson( "{\"markTask\":true, \"taskId\": "+resp.getId()+"}", TaskRequest.class);
    	ManaglyResponse markedResp = new MarkTaskHandler().handleRequest(markTaskRequest, createContext(""));
    	Assert.assertEquals("Task marked successfully", markedResp.getClass(), GenericSuccessResponse.class);
    }
    @Test
    public void markMissingTask() throws IOException {
    	TaskRequest markTaskRequest = new Gson().fromJson( "{\"markTask\":true, \"taskId\": 999}", TaskRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		new MarkTaskHandler().handleRequest(markTaskRequest, createContext(""));
		});
    }

}
