package uk.ac.bris.cs.databases;

import java.util.List;
import java.util.ArrayList;
import uk.ac.bris.cs.databases.api.AdvancedForumSummaryView;
import uk.ac.bris.cs.databases.api.TopicSummaryView;

import org.junit.Test;
import static org.junit.Assert.*;

public class GetAdvancedForumsTest extends TestBase {

    @Test
    public void testLastTopic() {
        List<AdvancedForumSummaryView> list = ok(api.getAdvancedForums());
        AdvancedForumSummaryView forum = list.get(0);
        TopicSummaryView topic = forum.getLastTopic();
        assertEquals(topic.getTitle(), "testtopic 1");
        assertEquals(topic.getLastPostTime(), 18);
        assertEquals(topic.getPostCount(), 14);
        assertEquals(topic.getLikes(), 2);
        assertEquals(topic.getCreated(), 0);
    }

    @Test
    public void testForumName() {
        List<AdvancedForumSummaryView> list = ok(api.getAdvancedForums());
        AdvancedForumSummaryView forum = list.get(0);
        assertEquals(forum.getTitle(), "test forum 1");
    }
}
