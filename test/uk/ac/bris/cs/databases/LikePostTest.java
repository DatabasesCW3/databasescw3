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

public class LikePostTest extends TestBase {
	
    @Test public void testLikePost() {
        ok(api.likePost("uname 1", 1, 1, true));
        ok(api.likePost("uname 1", 1, 10, true));
        ok(api.likePost("uname 1", 1, 10, false));
        
        //Failure if person or topic or post doesn't exist
        mustfail(api.likePost("uname 2035", 1, 1, true));
        mustfail(api.likePost("uname 1", 1526, 1, true));
        mustfail(api.likePost("uname 1", 1526, 8461, true));
    }
}
