package uk.ac.bris.cs.databases;

import  java.util.List;
import  java.util.ArrayList;
import uk.ac.bris.cs.databases.api.SimpleForumSummaryView;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by berk on 19/04/16.
 */
public class GetSimpleForumsTest extends TestBase {

    @Test
    public void testGetSimpleForums() {
        List<SimpleForumSummaryView> list = new ArrayList<>();
        list = ok(api.getSimpleForums());
        assertFalse(list.isEmpty());

        SimpleForumSummaryView summaryView = list.get(0);
        assertEquals("test forum 1", summaryView.getTitle());
        assertEquals(1, summaryView.getId());

        SimpleForumSummaryView secondSummaryView = list.get(1);
        assertEquals("test forum 2", secondSummaryView.getTitle());
        assertEquals(2, secondSummaryView.getId());

    }
}
