package uk.ac.bris.cs.databases;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by berk on 03/05/16.
 */
public class CreatePostTest extends TestBase {
    @Test
    public void testCreatePost() {

        ok(api.createPost(1, "user 1", "This is the body of the post"));
        assertEquals(15, api.countPostsInTopic(1));
    }
}
