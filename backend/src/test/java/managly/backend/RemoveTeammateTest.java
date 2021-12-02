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
public class RemoveTeammateTest extends LambdaTest {	
    @Test
    public void deleteTeammate() throws IOException {
    	//Create Project
    	//Create Task + add to Project
    	//Create subtask + add to Project
    	//Create Teammate + add to Project
    	//Create TaskAssignment + add to Project
    	//Delete Teammate
    	//Verify Teammate & TaskAssignment are deleted.
    }
    
    @Test
    public void deleteMissingTeammate() throws IOException {
    	//Create project
    	//Call delete + fail
    }
    
    @Test
    public void deleteMissingTeammateFromMissingProject() throws IOException {
    	//Call delete + fail
    }
    
    @Test
    public void deleteMissingTeammateFromLockedProject() throws IOException {
    	//Create Project + Lock
    	//Create teammate
    	//Call delete + fail
    }
}
