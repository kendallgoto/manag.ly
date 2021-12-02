package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
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
    	//Create Project
    	//Create Task + add to Project
    	//Create second Task + add to Project
    }
    
    @Test
    public void addSubTaskToTask() throws IOException {
    	//Create Project
    	//Create Task + add to Project
    	//Create subtask + add to Task
    	//Create sub-sub-task + add to subtask.
    }
    
    @Test
    public void addTaskToMissingProject() throws IOException {
    	//Create Task and fail
    }
    
    @Test
    public void addTaskToLockedProject() throws IOException {
    	//Create Project + Archive
    	//Create Task and fail
    }


}
