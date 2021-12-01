package managly.backend;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;

import com.amazonaws.services.lambda.runtime.Context;

import managly.backend.db.DatabaseUtil;

public class LambdaTest {
	
	/**
	 * Helper method that creates a context that supports logging so you can test lambda functions
	 * in JUnit without worrying about the logger anymore.
	 * 
	 * @param apiCall      An arbitrary string to identify which API is being called.
	 * @return
	 */
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	@BeforeEach
	public void cleanup() {
		try {
			System.out.println("Cleaning up!");
			Connection conn = DatabaseUtil.connect();
			conn.setAutoCommit(false);
			
			Statement disableFK = conn.createStatement();
			disableFK.execute("SET FOREIGN_KEY_CHECKS=0");
			disableFK.close();
			
	        conn.prepareStatement("TRUNCATE taskAssignments;").execute();
	        conn.prepareStatement("TRUNCATE teammates;").execute();
	        conn.prepareStatement("TRUNCATE projects;").execute();
	        conn.prepareStatement("TRUNCATE tasks;").execute();
	        conn.commit();
	        
			Statement enableFK = conn.createStatement();
			enableFK.execute("SET FOREIGN_KEY_CHECKS=1");
			enableFK.close();
			conn.setAutoCommit(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}
