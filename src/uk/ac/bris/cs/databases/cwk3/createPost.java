package uk.ac.bris.cs.databases.cwk3;

import java.sql.*;
import java.util.Date;
import uk.ac.bris.cs.databases.api.Result;

public class CreatePost {
    private Connection c;
    private final String userCheck = "SELECT * FROM Person WHERE Person.username = ?";
    private final String getPostsTable = " SELECT MAX(postNumber) as postNumber"
                                       + " FROM Topic"
                                       + " INNER JOIN Post ON Topic.id = Post.topic"
                                       + " WHERE Topic.id = ?";
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
        try (PreparedStatement p = c.prepareStatement(statement)) {
                Long userID = getUserIDForUsername(username);
                if (userID == null) {
                    return Result.failure("User does not exist!");
                } else {
                    int postNumber = getPostsTable(topicID);
                    if (postNumber == 0) {
                        return Result.failure("That topic does not exist!");
                    }
                    p.setString(1, username);
                    p.setLong(2, topicID);
                    p.setInt(3, (int) new Date().getTime());
                    p.setInt(4, postNumber + 1);
                    p.setString(5, text);
                    p.execute();
                    c.commit();
                    return Result.success();
                }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private int getPostsTable(long topicID) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(getPostsTable)) {
            p.setLong(1, topicID);
            try (ResultSet r = p.executeQuery()) {
                if (!r.isBeforeFirst()) {
                    return 0;
                }
                else {
                    return r.getInt("postNumber");
                }
            }
        }
    }

    private Long getUserIDForUsername(String username) throws SQLException {
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
