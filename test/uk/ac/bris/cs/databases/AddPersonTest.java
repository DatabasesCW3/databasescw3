package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import static org.junit.Assert.*;

public class AddPersonTest extends TestBase {

    @Test public void testAddPerson() {
        Map<String, String> users = ok(api.getUsers());

        ok(api.addNewPerson("David", "csxdb", null));
        users = ok(api.getUsers());
        assertEquals(6, users.size());
        assertEquals("David", users.get("csxdb"));

        mustfail(api.addNewPerson("David", "csxdb", null));
        users = ok(api.getUsers());
        assertEquals(6, users.size());

        ok(api.addNewPerson("David", "csxd2", null));
        users = ok(api.getUsers());
        assertEquals(7, users.size());
        assertEquals("David", users.get("csxdb"));
        assertEquals("David", users.get("csxd2"));

        removeTestPeople();
    }

    private void removeTestPeople() {
        try {
            c.prepareStatement("DELETE FROM Person WHERE name = 'David'").executeUpdate();
            c.commit();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
