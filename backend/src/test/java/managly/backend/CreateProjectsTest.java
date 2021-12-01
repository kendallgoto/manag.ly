package managly.backend;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateProjectsTest extends LambdaTest {
	@BeforeEach
	public void cleanup() {
		LocalTest.cleanup();
	}
	
    @Test
    public void testGoodCreate() throws IOException {
    	CreateProjectHandler handler = new CreateProjectHandler();
    	ProjectRequest req = new Gson().fromJson( "{\"title\":\"my project test\"}", ProjectRequest.class);
    	ManaglyResponse response = handler.handleRequest(req, createContext(""));
    	Assert.assertEquals("Unique project successfully created", response.getClass(), ProjectResponse.class);
    }
    
    @Test
    public void testDuplicateCreate() throws IOException {
    	CreateProjectHandler handler = new CreateProjectHandler();
    	ProjectRequest req = new Gson().fromJson( "{\"title\":\"my project test\"}", ProjectRequest.class);
    	ManaglyResponse response = handler.handleRequest(req, createContext(""));
    	Assert.assertEquals("First project successfully created", response.getClass(), ProjectResponse.class);
    	
		Assertions.assertThrows(GenericErrorResponse.class, () -> {
	    	handler.handleRequest(req, createContext("")); // run again to create duplicate
		});
    }
}
