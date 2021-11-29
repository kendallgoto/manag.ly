package managly.backend;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import managly.backend.db.DatabaseUtil;
import managly.backend.db.ProjectDocument;
import managly.backend.db.TaskDocument;
import managly.backend.db.TeammateDocument;

public class LocalTest {
	@BeforeAll
	public static void cleanup() {
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

    @Test
    public void testCreateProject() throws SQLException {
    	cleanup();
		ProjectDocument project = new ProjectDocument();
		Assert.assertFalse("No First Project", project.findById(1));
		
		ProjectDocument newProject = new ProjectDocument("My Cool Project");
		Assert.assertTrue("First Project Saved", newProject.save()); // create id=1
		
		ProjectDocument secondCopy = new ProjectDocument();
		Assert.assertTrue("First Project Exists", secondCopy.findById(1)); // id=1 exists
		Assert.assertTrue("First Project matches Created", newProject.getObject().equals(secondCopy.getObject())); // objects match
		
		ProjectDocument conflictedTitle = new ProjectDocument("My Cool Project");
		Assertions.assertThrows(SQLException.class, () -> {
			conflictedTitle.save();
		});
		secondCopy.getObject().setTitle("My renamed project");
		Assert.assertTrue("Saved new title on first project", secondCopy.save());
		
		Assert.assertTrue("Conflicted Title no longer conflicted", conflictedTitle.save());
		Assert.assertTrue("Second project is saved", new ProjectDocument().findById(3));
		Assert.assertTrue("Deleted second project", conflictedTitle.delete());
		Assert.assertFalse("Confirm second project deleted", new ProjectDocument().findById(3));
    }
    
    @Test
    public void testCreateTeammate() throws SQLException {
		ProjectDocument teammateProject = new ProjectDocument("Teammate Test Project");
		Assert.assertTrue("Teammate Test Save", teammateProject.save());
		
		TeammateDocument teammateOne = new TeammateDocument("Teammate One", teammateProject.getObject().getId());
		Assert.assertTrue("Teammate one saved", teammateOne.save());
		
		TeammateDocument teammateTwo = new TeammateDocument("Teammate Two", teammateProject.getObject().getId());
		Assert.assertTrue("Teammate two saved", teammateTwo.save());
		
		ProjectDocument findTeammates = new ProjectDocument();
		Assert.assertTrue("Rediscover task test project", findTeammates.findById(teammateProject.getObject().getId()));
		
		Assert.assertEquals("Task Test Project has two tasks", 2, findTeammates.populateTeammates());
		Assert.assertTrue("task one exists as child", findTeammates.getTeammates().get(0).getObject().equals(teammateOne.getObject()));
		Assert.assertTrue("task two exists as child", findTeammates.getTeammates().get(1).getObject().equals(teammateTwo.getObject()));
    }
    
    @Test
    public void testCreateTask() throws SQLException {
    	ProjectDocument taskProject = new ProjectDocument("Task Test Project");
		Assert.assertTrue("Task Test Save", taskProject.save());
		
		TaskDocument taskOne = new TaskDocument("1.", "My first task", taskProject.getObject().getId());
		Assert.assertTrue("Task one saved", taskOne.save());

		TaskDocument taskTwo = new TaskDocument("2.", "My second task", taskProject.getObject().getId());
		Assert.assertTrue("Task two saved", taskTwo.save());
		
		ProjectDocument findTasks = new ProjectDocument();
		Assert.assertTrue("Rediscover task test project", findTasks.findById(taskProject.getObject().getId()));
		
		Assert.assertEquals("Task Test Project has two tasks", 2, findTasks.populateTasks());
		Assert.assertTrue("task one exists as child", findTasks.getTasks().get(0).getObject().equals(taskOne.getObject()));
		Assert.assertTrue("task two exists as child", findTasks.getTasks().get(1).getObject().equals(taskTwo.getObject()));
    }
    

}
