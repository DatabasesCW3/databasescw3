package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author David
 */
public class CreatePersonTest extends TestBase {
    
    @Test public void testCreatePerson() {
        Map<String, String> users = ok(api.getUsers());
        assertTrue(users.isEmpty());
        
        ok(api.addNewPerson("David", "csxdb", null));
        users = ok(api.getUsers());
        assertEquals(1, users.size());
        assertEquals("David", users.get("csxdb"));
        
        mustfail(api.addNewPerson("David", "csxdb", null));
        users = ok(api.getUsers());
        assertEquals(1, users.size());
        
        ok(api.addNewPerson("David", "csxd2", null));
        users = ok(api.getUsers());
        assertEquals(2, users.size());
        assertEquals("David", users.get("csxdb"));
        assertEquals("David", users.get("csxd2"));
    }
}
