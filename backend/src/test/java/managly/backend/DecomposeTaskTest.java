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
    public void createDecompose() throws IOException, SQLException {
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	
    	AddTaskHandler handler = new AddTaskHandler();
    	TaskRequest createTaskReq = new Gson().fromJson( "{\"name\":\"My Super Cool First Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	handler.handleRequest(createTaskReq, createContext(""));

    	TaskRequest createSecondReq = new Gson().fromJson( "{\"name\":\"My Less Cool Second Task\", \"projectId\": "+projId+"}", TaskRequest.class);
    	handler.handleRequest(createSecondReq, createContext(""));

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
    	
    }
}
