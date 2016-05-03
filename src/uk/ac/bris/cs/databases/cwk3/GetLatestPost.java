package uk.ac.bris.cs.databases.cwk3;

import uk.ac.bris.cs.databases.api.PostView;
import uk.ac.bris.cs.databases.api.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by berk on 28/04/16.
 */
public class GetLatestPost {
    private Connection c;
    private final String checkForExistingTopic = "SELECT *"
                                        + " FROM Topic"
                                        + " WHERE id = ?";
    private final String statement = "SELECT *"
                                    + " FROM Post "
                                    + " JOIN Topic ON Post.topic = Topic.id"
                                    + " JOIN Person ON Post.user = Person.id"
                                    + " LEFT JOIN LikesPost ON Post.id = LikesPost.post"
                                    + " WHERE topic = ?"
                                    + " ORDER BY post.postedAt";
    GetLatestPost(Connection c) {
        this.c = c;
    }

    public Result<PostView> run(long topicID) {
        try {
            boolean topicExists = checkForExistingTopic(topicID);
            if (!topicExists) {
                return Result.failure("Topic with that TopicID does not exist!");
            }
        } catch (SQLException e) {
            Result.fatal(e.getMessage());
        }

        try(PreparedStatement p = c.prepareStatement(statement)) {
            ResultSet results = p.executeQuery();
            if (!results.isBeforeFirst()) {
                return Result.failure("There are no posts in this topic!");
            }

            return Result.success(getPost(results));

        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private PostView getPost(ResultSet results) throws SQLException {
        results.next();
        long forumID = results.getLong("forum");
        long topicID = results.getLong("topic");

        int postNumber = results.getInt("postNumber");

        String authorName = results.getString("name");
        String authorUsername = results.getString("username");
        String text = results.getString("body");

        int postedAt = results.getInt("postedAt");
        int likes = results.getInt("likes");

        return new PostView(forumID, topicID, postNumber, authorName, authorUsername, text, postedAt, likes);
    }

    private boolean checkForExistingTopic(long topicID) throws SQLException {
        try(PreparedStatement p = c.prepareStatement(checkForExistingTopic)) {
            p.setLong(1, topicID);
            ResultSet r = p.executeQuery();
            return (r.isBeforeFirst());
        }
    }
}
