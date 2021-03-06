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
public class DecomposeTaskTest extends LambdaTest {	
    @Test
    public void createDecompose() throws IOException {
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	
    	TeammateRequest teammateOne = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	new AddTeammateHandler().handleRequest(teammateOne, createContext(""));
    	TeammateRequest teammateTwo = new Gson().fromJson( "{\"name\":\"Second Teammate\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	new AddTeammateHandler().handleRequest(teammateTwo, createContext(""));
    	
    	AddTaskHandler handler = new AddTaskHandler();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	handler.handleRequest(createTaskReq, createContext(""));
    		
    	TaskRequest createSecondReq = new Gson().fromJson( "{\"name\":\"My Less Cool Second Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	handler.handleRequest(createSecondReq, createContext(""));

    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": 1, \"taskId\": 2}" , AssignmentRequest.class);
    	new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	AssignmentRequest assignTwo = new Gson().fromJson( "{\"teammateId\": 2, \"taskId\": 2}" , AssignmentRequest.class);
    	new AssignTeammateHandler().handleRequest(assignTwo, createContext(""));

    	DecomposeRequest decomposeRequest = new Gson().fromJson( "{\n" + 
    			"    \"taskId\": 2,\n" + 
    			"    \"subtasks\": [\n" + 
    			"        {\n" + 
    			"            \"name\": \"My First Subtask\"\n" + 
    			"        },\n" + 
    			"        {\n" + 
    			"            \"name\": \"My second subtask\"\n" + 
    			"        }\n" + 
    			"    ]\n" + 
    			"}", DecomposeRequest.class);
    	ManaglyResponse resp = new DecomposeTaskHandler().handleRequest(decomposeRequest, createContext(""));
    	Assert.assertEquals("Task successfully decomposed", resp.getClass(), TaskResponse.class);
    	TaskResponse decomposedBaseTask = (TaskResponse) resp;
    	
    	Assert.assertEquals("First decomposed task is correct", decomposedBaseTask.subtasks[0].getName(), "My First Subtask");
    	Assert.assertEquals("First decomposed task number is correct", decomposedBaseTask.subtasks[0].getTaskNumber(), "2.1.");

    	Assert.assertEquals("Second decomposed task is correct", decomposedBaseTask.subtasks[1].getName(), "My second subtask");
    	Assert.assertEquals("Second decomposed task number is correct", decomposedBaseTask.subtasks[1].getTaskNumber(), "2.2.");
    	//Get deep ...
    	ProjectRequest fetchRequest = new Gson().fromJson( "{\"projectId\": "+projId+"}", ProjectRequest.class);
    	ManaglyResponse foundProj = new GetProjectHandler().handleRequest(fetchRequest, createContext(""));
    	Assert.assertEquals("Rediscovered project", foundProj.getClass(), ProjectResponse.class);
    	
    	//Add task to new subtask list ...
    	TaskRequest addSubtask = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+", \"taskParent\": "+2+"}", TaskRequest.class);
    	ManaglyResponse appendedResp = new AddTaskHandler().handleRequest(addSubtask, createContext(""));
    	Assert.assertEquals("Appended Subtask Successfully", appendedResp.getClass(), TaskResponse.class);
    	TaskResponse appendedResp_task = (TaskResponse)appendedResp;
    	Assert.assertEquals("Appended Subtask has correct number", appendedResp_task.getTaskNumber(), "2.3.");

    }
    @Test
    public void decomposeInvalidTask() throws IOException {
    	DecomposeRequest decomposeRequest = new Gson().fromJson( "{\n" + 
    			"    \"taskId\": 999,\n" + 
    			"    \"subtasks\": [\n" + 
    			"        {\n" + 
    			"            \"name\": \"My First Subtask\"\n" + 
    			"        },\n" + 
    			"        {\n" + 
    			"            \"name\": \"My second subtask\"\n" + 
    			"        }\n" + 
    			"    ]\n" + 
    			"}", DecomposeRequest.class);
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
			new DecomposeTaskHandler().handleRequest(decomposeRequest, createContext(""));
		});
    }
    
    @Test
    public void decomposeAlreadyDecomposed() throws IOException {
    	createDecompose();
    	DecomposeRequest decomposeRequest = new Gson().fromJson( "{\n" + 
    			"    \"taskId\": 2,\n" + 
    			"    \"subtasks\": [\n" + 
    			"        {\n" + 
    			"            \"name\": \"My First Subtask\"\n" + 
    			"        },\n" + 
    			"        {\n" + 
    			"            \"name\": \"My second subtask\"\n" + 
    			"        }\n" + 
    			"    ]\n" + 
    			"}", DecomposeRequest.class);
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
			new DecomposeTaskHandler().handleRequest(decomposeRequest, createContext(""));
		});
    }
}
