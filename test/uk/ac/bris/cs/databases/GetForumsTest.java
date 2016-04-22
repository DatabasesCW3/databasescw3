package uk.ac.bris.cs.databases;

import java.util.List;
import java.util.ArrayList;
import uk.ac.bris.cs.databases.api.ForumSummaryView;
import uk.ac.bris.cs.databases.api.SimpleTopicSummaryView;

import org.junit.Test;
import static org.junit.Assert.*;

public class GetForumsTest extends TestBase {

    @Test
    public void testLastTopic() {
        List<ForumSummaryView> list = ok(api.getForums());
        ForumSummaryView forum = list.get(0);
        SimpleTopicSummaryView topic = forum.getLastTopic();
        assertEquals(topic.getTitle(), "testtopic 1");
    }

    @Test
    public void testForumName() {
        List<ForumSummaryView> list = ok(api.getForums());
        ForumSummaryView forum = list.get(0);
        assertEquals(forum.getTitle(), "test forum 1");
    }
}
