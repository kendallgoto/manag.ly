package managly.backend;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import managly.backend.http.*;

public class UnassignTeammateTest extends LambdaTest {
	
	@Test
	public void UnassignTeammateFromTask() throws IOException  {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = (TeammateResponse) new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	TaskResponse task = (TaskResponse) new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	ManaglyResponse response = new UnassignTeammateHandler().handleRequest(assignReq, createContext(""));
    	Assert.assertEquals("Teammate successfully unassigned from task", response.getClass(), GenericSuccessResponse.class);
	}
			
	@Test
	public void UnassignTeammateFromTaskInDifferentProject()throws IOException {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	CreateProjectHandler projectHandler = new CreateProjectHandler();
    	ProjectRequest projectReq = new Gson().fromJson( "{\"title\":\"my project test2\"}", ProjectRequest.class);
    	ProjectResponse projectResponse = (ProjectResponse)projectHandler.handleRequest(projectReq, createContext(""));
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = (TeammateResponse) new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+ projectResponse.getId()+ "}", TaskRequest.class);
    	TaskResponse task = (TaskResponse) new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		new UnassignTeammateHandler().handleRequest(assignReq, createContext(""));
    	});
	}
	
	@Test
	public void UnassignTeammateFromEmptyProject()throws IOException {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = (TeammateResponse) new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + 0 +"}" , AssignmentRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		new UnassignTeammateHandler().handleRequest(assignReq, createContext(""));
    	});
	}
	
	@Test
	public void UnassignTeammateFromTaskTwice()throws IOException {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = (TeammateResponse) new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	TaskResponse task = (TaskResponse) new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	new UnassignTeammateHandler().handleRequest(assignReq, createContext(""));
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		new UnassignTeammateHandler().handleRequest(assignReq, createContext(""));
    	});
	}
	
	@Test
	public void UnassignFakeTeammateFromTask()throws IOException {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	TaskResponse task = (TaskResponse) new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + 0 + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () ->  {
    		new UnassignTeammateHandler().handleRequest(assignReq, createContext(""));
		});
	}

}
