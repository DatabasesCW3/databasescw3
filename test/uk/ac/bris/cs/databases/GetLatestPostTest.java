package uk.ac.bris.cs.databases;

import org.junit.Test;
import uk.ac.bris.cs.databases.TestBase;
import uk.ac.bris.cs.databases.api.Result;

import java.sql.ResultSet;

/**
 * Created by berk on 28/04/16.
 */
public class GetLatestPostTest extends TestBase {

    @Test
    public void testGetLatestPost() {
        ok(Result.failure("bad"));
    }
}
