package uk.ac.bris.cs.databases;

import java.util.List;
import java.util.ArrayList;
import uk.ac.bris.cs.databases.api.AdvancedForumView;
import uk.ac.bris.cs.databases.api.TopicSummaryView;

import org.junit.Test;
import static org.junit.Assert.*;

public class GetAdvancedForumTest extends TestBase {

    @Test
    public void testLastTopic() {
        AdvancedForumView forum = ok(api.getAdvancedForum(1));
        List<TopicSummaryView> topics = forum.getTopics();
        TopicSummaryView topic = topics.get(0);
        assertEquals(topic.getTitle(), "testtopic 1");
        assertEquals(topic.getLastPostTime(), 18);
        assertEquals(topic.getPostCount(), 14);
        assertEquals(topic.getLikes(), 2);
        assertEquals(topic.getCreated(), 0);

        TopicSummaryView topic2 = topics.get(1);
        assertEquals(topic2.getTitle(), "testtopic 3");
        assertEquals(topic2.getLastPostTime(), 7);
        assertEquals(topic2.getPostCount(), 1);
        assertEquals(topic2.getLikes(), 0);
        assertEquals(topic2.getCreated(), 7);

    }
}
