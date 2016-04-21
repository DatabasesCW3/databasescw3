package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import uk.ac.bris.cs.databases.api.PersonView;

import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class CountPostsInTopicTest extends TestBase {
    
    @Test public void testCountPostsInTopic() {
		assertEquals(4, api.countPostsInTopic(1));
		assertEquals(1, api.countPostsInTopic(2));
		
		mustfail(api.countPostsInTopic(3));
    }
}
