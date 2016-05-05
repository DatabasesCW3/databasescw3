package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.SimpleTopicSummaryView;
import uk.ac.bris.cs.databases.api.ForumSummaryView;

import java.util.Date;

class LikePost {
    private Connection c;
    private Integer userId;
    private Boolean exists;

    public LikePost(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
    }

    public Result run(String username, long topicId, int post, boolean like) {
        if (!validate(username)) {
            return Result.failure("Invalid input");
        }
        try {
            initialise(username, topicId, post);
            if (exists) {
                addLike(post, like);
                c.commit();
                return Result.success();
            } else {
                c.rollback();
                return Result.failure("Username or topic nonexistent");
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }
    
    private void addLike(int post, boolean like) throws SQLException {
        if (likeExists(post)) {
            if (!like) {
                try ( PreparedStatement p = c.prepareStatement("DELETE FROM LikesPost WHERE user = ? AND post = ?")) {
                    p.setInt(1, userId);
                    p.setInt(2, post);
                    p.execute();
                }
            }
        } else {
            if (like) {
                try ( PreparedStatement p = c.prepareStatement("INSERT INTO LikesPost(user, post) VALUES(?, ?)")) {
                    p.setInt(1, userId);
                    p.setInt(2, post);
                    p.execute();
                }
            }
        }
    }
    
    public Boolean likeExists(int post) {
        try(PreparedStatement p = c.prepareStatement("SELECT * FROM LikesPost WHERE user = ? AND post = ?")) {
            p.setInt(1, userId);
            p.setInt(2, post);
            
            ResultSet r = p.executeQuery();
            if (r.isBeforeFirst()) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    private boolean validate(String username) {
        if (username == null) { return false; }
        if ("".equals(username)) { return false; }
        return true;
    }

    private void initialise(String username, long topicId, int post) throws SQLException {
        String check = "SELECT "
                     + "EXISTS (SELECT 1 FROM Person WHERE Person.username = ?) AS unameExists, "
                     + "EXISTS (SELECT 1 FROM Topic WHERE Topic.id = ?) AS forumExists, "
                     + "EXISTS (SELECT 1 FROM Post WHERE Post.id = ?) AS postExists, "
                     + "(SELECT Person.id FROM Person WHERE Person.username = ? ) AS userId "
                     + "FROM Person;";
        
        try (PreparedStatement p = c.prepareStatement(check)) {
            p.setString(1, username);
            p.setLong(2, topicId);
            p.setInt(3, post);
            p.setString(4, username);
            try (ResultSet r = p.executeQuery()) {
                if (!r.isBeforeFirst() ) { exists = false; }
                Boolean u = r.getBoolean("unameExists");
                Boolean f = r.getBoolean("forumExists");
                Boolean po = r.getBoolean("postExists");
                exists = u & f & po;
                userId = r.getInt("userId");
            }
        }
    }
}
