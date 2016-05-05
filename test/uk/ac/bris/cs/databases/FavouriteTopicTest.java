package uk.ac.bris.cs.databases;

import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by berk on 04/05/16.
 */
public class FavouriteTopicTest extends TestBase {

    @Test
    public void testFavouriteTopic() {
        ok(api.favouriteTopic("uname 1", 1, false));
        ok(api.favouriteTopic("uname 1", 1, true));
    }

    @Test
    public void testInvalidUsername() {
        mustfail(api.favouriteTopic("invalid username", 1, true));
    }

    @Test
    public void testInvalidTopic() {

        mustfail(api.favouriteTopic("uname 4", 1000, true));
    }

    private void addFavouriteBackInAfterTest() {
        try {
            c.prepareStatement("INSERT INTO Favourite VALUES (null, 1, 1)").executeUpdate();
            c.commit();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
