package managly.backend;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import managly.backend.http.*;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ArchiveProjectTest extends LambdaTest {	
    @Test
    public void archiveProject() throws IOException {
    	ProjectResponse newProj = new CreateProjectTest().testGoodCreate();
    	int projId = newProj.getId();
    	    	
    	ProjectRequest archiveProjHandler = new Gson().fromJson( "{\"projectId\": "+projId+"}", ProjectRequest.class);
    	ManaglyResponse markedResp = new ArchiveProjectHandler().handleRequest(archiveProjHandler, createContext(""));
    	Assert.assertEquals("Project archived successfully", markedResp.getClass(), GenericSuccessResponse.class);
    }
    @Test
    public void archiveMissingProject() throws IOException {
    	ProjectRequest archiveProjHandler = new Gson().fromJson( "{\"projectId\": 999}", ProjectRequest.class);
    	Assertions.assertThrows(GenericErrorResponse.class, () -> {
        	new ArchiveProjectHandler().handleRequest(archiveProjHandler, createContext(""));
		});
    }
}
