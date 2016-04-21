package uk.ac.bris.cs.databases;

import java.util.List;
import java.util.ArrayList;
import uk.ac.bris.cs.databases.api.SimpleForumSummaryView;

import org.junit.*;
/**
 * Created by berk on 19/04/16.
 */
public class GetSimpleForumsTest extends TestBase {

    @Test
    public void testGetSimpleForums() {
        List<SimpleForumSummaryView> list = ok(api.getSimpleForums());

        // TODO: Update test to check correct forums are returned and in the right alphabetical order, once the
        // 'createForum()' method is implemented.
    }
}
