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
public class GetForumTest extends TestBase {
    
    @Test public void testGetForum() {
		ForumView fv = ok(api.getForum(1));
		assertEquals("test forum 1", fv.getTitle());
		assertEquals(1, fv.getId());
		
		mustfail(api.getForum(3));
    }
}
