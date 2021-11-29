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
public class CreateProjectsTest extends LambdaTest {
	@BeforeEach // this didnt work for me for some reason
	public void cleanup() {
		LocalTest.cleanup();
	}
	
    @Test
    public void testGoodCreate() throws IOException {
    	this.cleanup(); //remove once @beforeEach works
    	CreateProjectHandler handler = new CreateProjectHandler();
    	CreateProjectRequest req = new Gson().fromJson( "{\"project\":\"my project test\"}", CreateProjectRequest.class);
    	ManaglyResponse response = handler.handleRequest(req, createContext(""));
    	Assert.assertEquals(new GenericErrorResponse(200, "OK!").toString(), response.toString());
    }
    
    @Test
    public void testDuplicateCreate() throws IOException {
    	this.cleanup(); //remove once @beforeEach works
    	CreateProjectHandler handler = new CreateProjectHandler();
    	CreateProjectRequest req = new Gson().fromJson( "{\"project\":\"my project test\"}", CreateProjectRequest.class);
    	ManaglyResponse response =  handler.handleRequest(req, createContext(""));
    	Assert.assertEquals(new GenericErrorResponse(200, "OK!").toString(), response.toString());
    	response = handler.handleRequest(req, createContext(""));
    	Assert.assertEquals(new GenericErrorResponse(409, "Project with this name already exists").toString(), response.toString());
    }
}
