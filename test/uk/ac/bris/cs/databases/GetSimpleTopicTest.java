package uk.ac.bris.cs.databases;

import java.util.List;

import uk.ac.bris.cs.databases.api.SimplePostView;
import uk.ac.bris.cs.databases.api.SimpleTopicView;

import org.junit.Test;
import static org.junit.Assert.*;

public class GetSimpleTopicTest extends TestBase {

  @Test public void testSuccess() {
    SimpleTopicView topic = ok(api.getSimpleTopic(1));
    List<SimplePostView> posts = topic.getPosts();
    int i = 0;
    assertEquals(5, posts.size());

    for (SimplePostView post: posts) {
      i++;
      assertEquals(post.getText(), "test post " + i);
    }
  }



  @Test public void testFail() {
    mustfail(api.getSimpleTopic(34));
  }
}
