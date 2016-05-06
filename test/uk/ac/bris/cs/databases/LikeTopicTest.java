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
        ok(api.likeTopic("uname 5", 1, true));
        ok(api.likeTopic("uname 5", 1, false));
        
        //Failure if person or topic doesn't exist
        mustfail(api.likeTopic("uname 2035", 1, true));
        mustfail(api.likeTopic("uname 1", 1526, true));
    }
}
