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
public class AddTeammateTest extends LambdaTest {	
    @Test
    public void addTeammateToProject() throws IOException {
    	//Create Project
    	//Create Teammate + add to Project
    	//Create second Teammate + add to Project
    }
    
    @Test
    public void assignTaskToTeammate() throws IOException {
    	//Create Project
    	//Create Teammate + add to Project
    	//Create second Teammate + add to Project
    	//Create Task + add to Project
    	//Assign task to Teammate
    }
    
    @Test
    public void addTeammateToMissingProject() throws IOException {
    	//Create Teammate and fail
    }
    @Test
    public void addTeammateToLockedProject() throws IOException {
    	//Create Project + Archive
    	//Create Teammate and fail
    }

}
