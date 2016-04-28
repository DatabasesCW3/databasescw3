package uk.ac.bris.cs.databases;

import java.util.List;

import uk.ac.bris.cs.databases.api.PostView;
import uk.ac.bris.cs.databases.api.TopicView;

import org.junit.Test;
import static org.junit.Assert.*;

public class GetTopicTest extends TestBase {

    @Test public void testSuccess() {
        TopicView topic = ok(api.getTopic(1,1));
        List<PostView> posts = topic.getPosts();
        assertEquals(posts.size(), 10);
        PostView pv = posts.get(0);
        assertEquals(pv.getLikes(), 2);
        assertEquals(pv.getText(), "test post 1");
        assertEquals(pv.getAuthorUserName(), "uname 1");
    }

    @Test public void testPageSuccess() {
        TopicView topic = ok(api.getTopic(1,2));
        List<PostView> posts = topic.getPosts();
        assertEquals(posts.size(), 4);
        PostView pv = posts.get(0);
        assertEquals(pv.getLikes(), 1);
        assertEquals(pv.getText(), "test post 11");
        assertEquals(pv.getAuthorUserName(), "uname 4");

    }

    @Test public void testPageFail() {
        mustfail(api.getTopic(1,34));
    }


    @Test public void testFail() {
        mustfail(api.getTopic(34,0));
    }
}
