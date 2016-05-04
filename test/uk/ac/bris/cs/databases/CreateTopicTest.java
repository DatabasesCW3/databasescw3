package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import uk.ac.bris.cs.databases.api.TopicView;
import uk.ac.bris.cs.databases.api.PostView;

import static org.junit.Assert.*;

public class CreateTopicTest extends TestBase {

    @Test public void testAddTopic() {
        ok(api.createTopic(1, "uname 1", "REMOVE123", "REMOVE123BODY"));
        
        TopicView tv = ok(api.getTopic(5,0));
        PostView pv = tv.getPosts().get(0);
        assertEquals(pv.getText(), "test body");
        
        /* failure if forum does not exist, user does not exist, title or text 
         * empty */
        mustfail(api.createTopic(1, "uname 1", "REMOVE123", ""));
        mustfail(api.createTopic(1, "uname 1", "", "REMOVE123BODY"));
        mustfail(api.createTopic(1234, "uname 1", "REMOVE123", "REMOVE123BODY"));
        mustfail(api.createTopic(1, "nonexistent", "REMOVE123", "REMOVE123BODY"));

        removeTestTopics();
    }
    
    private void removeTestTopics() {
        try {
            c.prepareStatement("DELETE FROM Topic "
                             + "WHERE Topic.title = 'REMOVE123' ")
                             .executeUpdate();
            c.prepareStatement("DELETE FROM Post "
                             + "WHERE Post.body = 'REMOVE123BODY' ")
                             .executeUpdate();
            c.commit();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
