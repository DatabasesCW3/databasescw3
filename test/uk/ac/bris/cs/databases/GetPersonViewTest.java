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
public class GetPersonViewTest extends TestBase {
    
    @Test public void testGetPersonView() {
		PersonView pv = ok(api.getPersonView("uname 1"));
		assertEquals("testperson 1", pv.getName());
		assertEquals("uname 1", pv.getUsername());
		assertEquals("stuId 1", pv.getStudentId());
		
		mustfail(api.getPersonView("uname 135343"));
    }
}
