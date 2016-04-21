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
        assertTrue(!users.isEmpty());
        assertEquals(users.size(), 2);
        // Must test the method after adding users, once the addNewPerson method has been added
    }
}
