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
public class RenameTaskTest extends LambdaTest {	
    @Test
    public void renameTask() throws IOException {
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	
    	AddTaskHandler handler = new AddTaskHandler();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	ManaglyResponse response = handler.handleRequest(createTaskReq, createContext(""));

    	TaskRequest createSecondReq = new Gson().fromJson( "{\"name\":\"My Less Cool Second Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	handler.handleRequest(createSecondReq, createContext(""));

    	TaskResponse task_first = (TaskResponse) response;
    	
    	TaskRequest renameTask = new Gson().fromJson( "{\"name\":\"My Renamed Task\", \"taskId\": "+task_first.getId()+"}", TaskRequest.class);
    	ManaglyResponse renameResp = new RenameTaskHandler().handleRequest(renameTask, createContext(""));
    	Assert.assertEquals("Project archived successfully", renameResp.getClass(), TaskResponse.class);
    }
}
