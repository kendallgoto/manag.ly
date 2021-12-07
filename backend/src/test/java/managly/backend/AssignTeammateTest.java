package managly.backend;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import managly.backend.http.*;

public class AssignTeammateTest {
	@Test
	public void AssignTeammateToTask()  {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	TaskResponse task = new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	ManaglyResponse response = new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	Assert.assertEquals("Teammate successfully assigned to task", response.getClass(), GenericSuccessResponse.class);
	}
	@Test
	public void AssignTeammateToTaskInArchivedProject() {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	TaskResponse task = new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	//lock project
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	});
    	
	}
	@Test
	public void AssignTeammateToTaskInDifferentProject() {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	CreateProjectHandler projectHandler = new CreateProjectHandler();
    	ProjectRequest projectReq = new Gson().fromJson( "{\"title\":\"my project test\"}", ProjectRequest.class);
    	ProjectResponse projectResponse = (ProjectResponse)projectHandler.handleRequest(projectReq, createContext(""));
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+ projectResponse.getId()+ "}", TaskRequest.class);
    	TaskResponse task = new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	});
	}
	@Test
	public void AssignTeammateToEmptyProject() {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TeammateRequest teammateCreate = new Gson().fromJson( "{\"name\":\"John Smith\", \"projectId\":"+projId +"}", TeammateRequest.class);
    	TeammateResponse teammate = new AddTeammateHandler().handleRequest(teammateCreate, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + teammate.getId() + ", \"taskId\": " + 0 +"}" , AssignmentRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
    		new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	});
	}
	@Test
	public void AssignTeammateToTaskTwice() {
		this.AssignTeammateToTask();
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
			this.AssignTeammateToTask();
		});
	}
	@Test
	public void AssignFakeTeammateToTask() {
		ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	TaskResponse task = new AddTaskHandler().handleRequest(createTaskReq, createContext(""));
    	AssignmentRequest assignReq = new Gson().fromJson( "{\"teammateId\": " + 0 + ", \"taskId\": " + task.getId() +"}" , AssignmentRequest.class);
    	ManaglyResponse response = new AssignTeammateHandler().handleRequest(assignReq, createContext(""));
    	Assert.assertEquals("Teammate successfully assigned to task", response.getClass(), GenericSuccessResponse.class);
	}

}
