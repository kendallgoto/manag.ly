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
public class DeleteProjectTest extends LambdaTest {	
    @Test
    public void deleteProject() throws IOException {
    	//Create Project
    	//Create Task + add to Project
    	//Create subtask + add to Project
    	//Create Teammate + add to Project
    	//Create TaskAssignment + add to Project
    	//Delete Project
    	//Verify Teammate, Task, Subtask, TaskAssignment are deleted.
    }
    
    @Test
    public void deleteMissingProject() throws IOException {
    	//Call delete + fail
    }
}
