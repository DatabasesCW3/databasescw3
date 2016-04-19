package uk.ac.bris.cs.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import uk.ac.bris.cs.databases.api.APIProvider;
import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.cwk3.API;
import uk.ac.bris.cs.databases.cwk3.DBSetup;

/**
 * Base class with common test utility methods.
 * @author csxdb
 */
public abstract class TestBase {

    private static final String TEST_DATABASE = "jdbc:sqlite:database/testdb.sqlite3";
    Connection c;
    APIProvider api;

    /**
     * Check that a particular operation returns failure, not success or fatal.
     */
    void mustfail(Result r) {
        assertFalse(r.isSuccess());
        assertFalse(r.isFatal());
    }

    /**
     * Wrap an API call to ensure that it succeeds.
     * @param in The result of the call.
     * @return The contained value on success, otherwise assert failure.
     */
    <T> T ok(Result<T> in) {
        assertTrue(in.isSuccess());
        return in.getValue();
    }

    @Before
    public void setUp() {
        //System.err.println("Setting up test database");
        try {
            c = DriverManager.getConnection(TEST_DATABASE);
            c.setAutoCommit(false);
            DBSetup.setup(c);
            api = new API(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        //System.err.println("Removing test database");
        try {
            DBSetup.drop(c);
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
