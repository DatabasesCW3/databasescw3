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
		int topic1 = ok(api.countPostsInTopic(1));
		assertEquals(14, topic1);
		
		int topic2 = ok(api.countPostsInTopic(2));
		assertEquals(1, topic2);
		
		mustfail(api.countPostsInTopic(445));
    }
}
