package uk.ac.bris.cs.databases;

import org.junit.Test;
import uk.ac.bris.cs.databases.api.PostView;

import static org.junit.Assert.assertEquals;

/**
 * Created by berk on 28/04/16.
 */
public class GetLatestPostTest extends TestBase {

    @Test
    public void testGetLatestPost() {
        PostView postView = ok(api.getLatestPost(4));
        assertEquals(900, postView.getPostedAt());
    }
}
