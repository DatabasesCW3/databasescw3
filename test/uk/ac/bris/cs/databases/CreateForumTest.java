package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import uk.ac.bris.cs.databases.api.ForumView;

import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class CreateForumTest extends TestBase {
	
    @Test public void testCreateForum() {
		mustfail(api.getForum(4));
		api.createForum("test forum 4");
		
		ForumView fv = ok(api.getForum(4));
		assertEquals("test forum 4", fv.getTitle());
		assertEquals(4, fv.getId());
		
		removeTestForum();
    }

    private void removeTestForum() {
        try {
            c.prepareStatement("DELETE FROM Forum WHERE title = 'test forum 4'").executeUpdate();
            c.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
