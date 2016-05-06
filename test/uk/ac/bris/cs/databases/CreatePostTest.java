package uk.ac.bris.cs.databases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by berk on 03/05/16.
 */
public class CreatePostTest extends TestBase {

    @Before
    public void setup() {
        ok(api.createPost(1, "uname 1", "This is the body of text"));
    }
    @Test
    public void testCreatePost() {
        int posts = ok(api.countPostsInTopic(1));
        assertEquals(15, posts);
    }

    @After
    public void tearDown() {
          try {
              c.prepareStatement("DELETE FROM Post WHERE body = 'This is the body of text'").executeUpdate();
              c.commit();
          }
          catch (SQLException e) {
              System.out.println(e.getMessage());
          }
    }
}
