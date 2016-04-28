package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import uk.ac.bris.cs.databases.api.ForumView;

import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class CreateForumTest extends TestBase {
    
    @Test public void testCreateForum() {
		mustfail(api.getForum(3));
		api.createForum("test forum 3");
		
		/*ForumView fv = ok(api.getForum(3));
		assertEquals("test forum 3", fv.getTitle());
		assertEquals(3, fv.getId());*/
    }
}
