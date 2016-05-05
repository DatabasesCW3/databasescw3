package uk.ac.bris.cs.databases;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.After;

import java.sql.SQLException;
import java.sql.PreparedStatement;

import uk.ac.bris.cs.databases.api.TopicView;
import uk.ac.bris.cs.databases.api.PostView;

import static org.junit.Assert.*;

public class LikeTopicTest extends TestBase {
	
    @Test public void testLikeTopic() {
        ok(api.likeTopic("uname 1", 1, true));
        
        /*TopicView tv = ok(api.getTopic(5,0));
        PostView pv = tv.getPosts().get(0);
        assertEquals(pv.getText(), "REMOVE123BODY");*/
        
        /* failure if forum does not exist, user does not exist, title or text 
         * empty */
        /*mustfail(api.createTopic(1, "uname 1", "REMOVE123", ""));
        mustfail(api.createTopic(1, "uname 1", "", "REMOVE123BODY"));
        mustfail(api.createTopic(1234, "uname 1", "REMOVE123", "REMOVE123BODY"));
        mustfail(api.createTopic(1, "nonexistent", "REMOVE123", "REMOVE123BODY"));*/
    }
    
    /*@After
    public void removeTestTopics() {
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
    }*/
}
