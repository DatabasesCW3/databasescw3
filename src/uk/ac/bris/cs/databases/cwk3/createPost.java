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

public class CreatePost {
    private Connection c;
    private final String topicCheck = "SELECT * FROM Topic WHERE Topic.id = ?";
    private final String userCheck = "SELECT * FROM Person WHERE Person.username = ?";
    private final String statement = "INSERT INTO Post" +
                                    " VALUES (null, ?, ?, ?, ?, ?)";

    public CreatePost(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
    }

    public Result run(long topicID, String username, String text) {
        if (!validate(username, text)) {
            return Result.failure("Invalid input");
        }
        try ( PreparedStatement p = c.prepareStatement(statement)) {
            if (topicExists(topicID) && userExists(username)) {
                p.setString(1, username);
                p.setString(2, );
                p.setString(3, studentId);
                p.setString(4, studentId);
                p.setString(5, text);
                p.execute();
                c.commit();
                return Result.success();
            }
            else { return Result.failure("Username already exists"); }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private boolean topicExists(long topicID) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(topicCheck)) {
            p.setLong(1, topicID);
            try (ResultSet r = p.executeQuery()) {
                return (r.isBeforeFirst());
            }
        }
    }

    private Long userExists(String username) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(userCheck)) {
            p.setString(1, username);
            try (ResultSet r = p.executeQuery()) {
                if (r.isBeforeFirst()) {
                    return r.getLong("id");
                } else
                    return null;
            }
        }
    }

    private boolean validate(String username, String text) {
        if (username == null || "".equals(username)) { return false; }
        if ("".equals(text)) { return false; }
        return true;
    }
}
