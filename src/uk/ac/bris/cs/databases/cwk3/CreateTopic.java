package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.SimpleTopicSummaryView;
import uk.ac.bris.cs.databases.api.ForumSummaryView;

import java.util.Date;

class CreateTopic {
    private Connection c;
    private Long topicId;
    private Long userId;
    private Boolean exists;
    private final String check = " SELECT EXISTS "
                               + " (SELECT 1 FROM Person "
                               + " WHERE Person.username = ? )"
                               + " AS unameExists,"
                               + " EXISTS (SELECT 1 FROM Forum "
                               + " WHERE Forum.id = ? )"
                               + " AS forumExists,"
                               + " Person.id AS userId,"
                               + " MAX(Topic.id) + 1 AS topicId"
                               + " FROM Person, Topic"
                               + " WHERE Person.username = ? ;";

    private final String newTopic = "INSERT INTO Topic VALUES"
                                  + " ( ?, ?, ?, ? )";

    // First post in a topic always has post number 1:
    private final String newPost = "INSERT INTO Post" +
                                       " VALUES (null, ?, ?, ?, 1, ?)";

    public CreateTopic(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
    }

    public Result run(long forumId, String username, String title, String text) {
        if (!validate(username, title, text)) {
            return Result.failure("Invalid input");
        }
        try {
            initialise(forumId, username);
            if (exists) {
                addTopic(forumId, title);
                addPost(text);
                c.commit();
                return Result.success();
            }
            else {
                c.rollback();
                return Result.failure("Username or forum nonexistent");
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private void addTopic(long forumId, String title) throws SQLException {
        try ( PreparedStatement p = c.prepareStatement(newTopic)) {
            p.setLong(1, topicId);
            p.setLong(2, userId);
            p.setString(3, title);
            p.setLong(4, forumId);
            p.execute();
        }
    }

    private void addPost(String text) throws SQLException {
        try ( PreparedStatement p = c.prepareStatement(newPost)) {
            p.setLong(1, userId);
            p.setLong(2, topicId);
            p.setLong(3, new Date().getTime());
            p.setString(4, text);
            p.execute();
        }
    }

    private void initialise(long forumId, String username) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(check)) {
            p.setString(1, username);
            p.setLong(2, forumId);
            p.setString(3, username);
            try (ResultSet r = p.executeQuery()) {
                if (!r.isBeforeFirst() ) { exists = false; }
                Boolean u = r.getBoolean("unameExists");
                Boolean f = r.getBoolean("forumExists");
                exists = u & f;
                topicId = r.getLong("topicId");
                userId = r.getLong("userId");
            }
        }
    }

    private boolean validate(String username, String title, String text) {
        if (username == null) { return false; }
        if (title == null) { return false; }
        if (text == null) { return false; }
        if ("".equals(title)) { return false; }
        if ("".equals(text)) { return false; }
        return true;
    }
}
