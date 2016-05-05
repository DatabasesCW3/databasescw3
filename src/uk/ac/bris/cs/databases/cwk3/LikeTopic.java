package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.SimpleTopicSummaryView;
import uk.ac.bris.cs.databases.api.ForumSummaryView;

import java.util.Date;

class LikeTopic {
    private Connection c;
    private Integer userId;
    private Boolean exists;

    public LikeTopic(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
    }

    public Result run(String username, long topicId, boolean like) {
		if (!validate(username)) {
            return Result.failure("Invalid input");
        }
        try {
            initialise(username, topicId);
            if (exists) {
                addLike(topicId, like);
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
	
    private void addLike(long topicId, boolean like) throws SQLException {
		if (likeExists(topicId)) {
			if (!like) {
				try ( PreparedStatement p = c.prepareStatement("DELETE FROM LikesTopic WHERE user = ? AND topic = ?")) {
					p.setInt(1, userId);
					p.setLong(2, topicId);
					p.execute();
				}
			}
		} else {
			if (like) {
				try ( PreparedStatement p = c.prepareStatement("INSERT INTO LikesTopic(user, topic) VALUES(?, ?)")) {
					p.setInt(1, userId);
					p.setLong(2, topicId);
					p.execute();
				}
			}
		}
    }
	
    public Boolean likeExists(long topicId) {
        try(PreparedStatement p = c.prepareStatement("SELECT * FROM LikesTopic WHERE user = ? AND topic = ?")) {
			p.setInt(1, userId);
            p.setLong(2, topicId);
			
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

    private void initialise(String username, long topicId) throws SQLException {
		String check = "SELECT "
				     + "EXISTS (SELECT 1 FROM Person WHERE Person.username = ?) AS unameExists, "
				     + "EXISTS (SELECT 1 FROM Topic WHERE Topic.id = ?) AS forumExists, "
				     + "(SELECT Person.id FROM Person WHERE Person.username = ? ) AS userId "
				     + "FROM Person;";
		
        try (PreparedStatement p = c.prepareStatement(check)) {
            p.setString(1, username);
            p.setLong(2, topicId);
            p.setString(3, username);
            try (ResultSet r = p.executeQuery()) {
                if (!r.isBeforeFirst() ) { exists = false; }
                Boolean u = r.getBoolean("unameExists");
                Boolean f = r.getBoolean("forumExists");
                exists = u & f;
				userId = r.getInt("userId");
            }
        }
    }
}
