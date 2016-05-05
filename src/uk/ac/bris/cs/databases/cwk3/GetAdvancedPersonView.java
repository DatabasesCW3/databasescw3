package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.TopicSummaryView;
import uk.ac.bris.cs.databases.api.AdvancedPersonView;

import java.util.List;
import java.util.ArrayList;

public class GetAdvancedPersonView {

    private final String getTopicSummary = "SELECT"
    + " Forum.id              AS fid,"
    + " Topic.id              AS tid,"
    + " Topic.title           AS tTitle,"
    + " LastPost.lastPostTime AS lastPostTime,"
    + " LastPost.postNumber   AS postCount,"
    + " LastPerson.username   AS lastPoster,"
    + " FirstPost.postedAt    AS firstPostTime,"
    + " FirstPerson.username  AS firstPoster,"
    + " FirstPerson.name      AS firstPosterName,"
    + " COUNT(LikesTopic.id)  AS likes"
    + " FROM"
    + "     Forum LEFT JOIN ("
    + "     ((((Topic"
    + "     INNER JOIN"
    + "         (SELECT lp.id,"
    + "         lp.postNumber,"
    + "         lp.topic,"
    + "         lp.user,"
    + "         lp.postedAt AS lastPostTime FROM"
    + "             ((Forum LEFT JOIN Topic ON Topic.forum = Forum.id)"
    + "             INNER JOIN Post AS lp ON lp.topic = Topic.id)"
    + "             INNER JOIN"
    + "             (SELECT Topic.id AS btid, MAX(lp.postedAt) AS lpt FROM"
    + "             (Forum LEFT JOIN Topic ON Topic.forum = Forum.id)"
    + "             INNER JOIN Post AS lp ON lp.topic = Topic.id"
    + "             GROUP BY Topic.id)"
    + "             ON Topic.id = btid AND lastPostTime = lpt)"
    + "         AS LastPost ON LastPost.topic = Topic.id)"
    + "     LEFT JOIN LikesTopic ON LikesTopic.topic = Topic.id)"
    + "     INNER JOIN Person AS LastPerson ON LastPost.user = LastPerson.id)"
    + "     INNER JOIN Post AS FirstPost ON FirstPost.topic = Topic.id"
    + "     AND FirstPost.postNumber = 1)"
    + "     INNER JOIN Person AS FirstPerson ON FirstPost.user = FirstPerson.id)"
    + " ON Topic.forum = Forum.id"
    + " GROUP BY Topic.id"
    + " HAVING Topic.id IN"
    + "     (SELECT Topic.id FROM"
    + "         Person LEFT JOIN Favourite ON Person.id = Favourite.user"
    + "         LEFT JOIN Topic ON Topic.id = Favourite.topic"
    + "     WHERE Person.username = ? )"
    + " ORDER BY tTitle ASC;";

    private final String getUserInfo = "SELECT"
    + " Person.name, Person.username, Person.stuId,"
    + " (SELECT count(LikesTopic.user)"
    + " FROM Person"
    + "    LEFT JOIN Post ON Person.id = Post.user AND Post.postNumber = 1"
    + "    LEFT JOIN likesTopic ON Post.topic = LikesTopic.topic"
    + " WHERE Person.username = ? ) AS topicLikes,"
    + " (SELECT count(LikesPost.user)"
    + " FROM Person"
    + "    LEFT JOIN Post ON Person.id = Post.user"
    + "    LEFT JOIN LikesPost ON Post.id = LikesPost.post"
    + " WHERE Person.username = ? ) AS postLikes"
    + " FROM"
    + " Person WHERE Person.username = ? ;";

    private Connection c;
    private AdvancedPersonView user;
    private List<TopicSummaryView> favs;

    public GetAdvancedPersonView(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
        favs = new ArrayList<TopicSummaryView>();
    }

    public Result<AdvancedPersonView> run(String username) {
        try (PreparedStatement p = c.prepareStatement(getTopicSummary)) {
            p.setString(1, username);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    getTopic(r);
                }
                buildUser(username);
                return Result.success(user);
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private void buildUser(String username) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(getUserInfo)) {
            p.setString(1, username);
            p.setString(2, username);
            p.setString(3, username);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    String name = r.getString("name");
                    String studentId = r.getString("stuId");
                    int topicLikes = r.getInt("topicLikes");
                    int postLikes = r.getInt("postLikes");
                    user = new AdvancedPersonView(name,
                                                  username,
                                                  studentId,
                                                  topicLikes,
                                                  postLikes,
                                                  favs);
                }
                else {
                    throw new SQLException();
                }
            }
        }
    }

    private void getTopic(ResultSet r) throws SQLException {
        Long forumId           = r.getLong("fid");
        Long topicId           = r.getLong("tid");
        String topicTitle      = r.getString("tTitle");
        int postCount          = r.getInt("postCount");
        int likes              = r.getInt("likes");
        int lastPostTime       = r.getInt("lastPostTime");
        int firstPostTime      = r.getInt("firstPostTime");
        String lastPoster      = r.getString("lastPoster");
        String firstPoster     = r.getString("firstPoster");
        String firstPosterName = r.getString("firstPosterName");

        TopicSummaryView topic;

        if (r.wasNull()) { topic = null; }
        else {
            topic = new TopicSummaryView(topicId,
                                         forumId,
                                         topicTitle,
                                         postCount,
                                         firstPostTime,
                                         lastPostTime,
                                         lastPoster,
                                         likes,
                                         firstPoster,
                                         firstPosterName);
            favs.add(topic);
        }
    }
}
