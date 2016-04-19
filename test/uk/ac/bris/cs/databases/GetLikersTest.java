package uk.ac.bris.cs.databases;

import java.util.List;

import uk.ac.bris.cs.databases.api.PersonView;

import org.junit.Test;
import static org.junit.Assert.*;

public class GetLikersTest extends TestBase {

  @Test public void testSuccess() {
    List<PersonView> users = ok(api.getLikers(1));
    assertEquals(1, users.size());
    PersonView pv = users.get(0);
    assertEquals(pv.getName(), "dude");
    assertEquals(pv.getUsername(), "uname");
    assertEquals(pv.getStudentId(), "uid");
  }

  @Test public void testEmptySuccess() {
    List<PersonView> users = ok(api.getLikers(2));
    assertTrue(users.isEmpty());
  }

  @Test public void testFail() {
    mustfail(api.getLikers(3));
  }
}
