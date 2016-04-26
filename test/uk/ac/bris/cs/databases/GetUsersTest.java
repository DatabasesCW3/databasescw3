package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by berk on 19/04/16.
 */
public class GetUsersTest extends TestBase {
    @Test
    public void testGetUsers() {
        Map<String, String> users = ok(api.getUsers());

        assertFalse(users.isEmpty());
        assertEquals(null, 5, users.size());

        String name = users.get("uname 2");
        assertEquals("testperson 2", name);

        assertNull(users.get("uname 6"));
    }
}
