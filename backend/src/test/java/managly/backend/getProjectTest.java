package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class getProjectTest extends LambdaTest {	
    @Test
    public void testExistingProject() throws IOException {
    	//Create a project
    	//Then find it with GetProjectHandler
    }
    
    @Test
    public void testMissingProject() throws IOException {
    	//Find a nonexisting project with GetProjectHandler
    }
}
