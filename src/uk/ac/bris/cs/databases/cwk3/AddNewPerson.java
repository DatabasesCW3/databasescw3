package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.SimpleTopicSummaryView;
import uk.ac.bris.cs.databases.api.ForumSummaryView;

import java.util.List;
import java.util.ArrayList;

class AddNewPerson {
    private Connection c;
    private final String check = "SELECT * FROM Person WHERE username = ? ";
    private final String statement = "INSERT INTO Person VALUES"
                                   + " ( null, ?, ?, ? )";

    public AddNewPerson(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
    }

    public Result run(String name, String username, String studentId) {
        if (!validate(name, username, studentId)) {
            return Result.failure("Invalid input");
        }
        try ( PreparedStatement p = c.prepareStatement(statement)) {
            if (!exists(username)) {
                p.setString(1, name);
                p.setString(2, username);
                p.setString(3, studentId);
                p.execute();
                c.commit();
                return Result.success();
            }
            else { return Result.failure("Username already exists"); }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private boolean exists(String username) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(check)) {
            p.setString(1, username);
            try (ResultSet r = p.executeQuery()) {
                if (r.isBeforeFirst() ) { return true; }
                else { return false; }
            }
        }
    }

    private boolean validate(String name, String username, String studentId) {
        if (name == null) { return false; }
        if (username == null) { return false; }
        if ("".equals(studentId)) { return false; }
        return true;
    }
}
