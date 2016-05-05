package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.TopicSummaryView;
import uk.ac.bris.cs.databases.api.AdvancedForumView;

import java.util.List;
import java.util.ArrayList;

class GetAdvancedForum {

    private final String statement = "SELECT"
    + " Forum.id              AS fid,"
    + " Forum.title           AS fTitle,"
    + " Topic.id              AS tid,"
    + " Topic.title           AS tTitle, "
    + " LastPost.lastPostTime AS lastPostTime,"
    + " LastPost.postNumber   AS postCount,"
    + " LastPerson.username   AS lastPoster,"
    + " FirstPost.postedAt    AS firstPostTime,"
    + " FirstPerson.username  AS firstPoster,"
    + " FirstPerson.name      AS firstPosterName,"
    + " COUNT(LikesTopic.id)  AS likes"
    + " FROM"
    + "     Forum LEFT JOIN ("
    + "     ((((Topic "
    + "     INNER JOIN "
    + "         (SELECT lp.id, "
    + "         lp.postNumber,"
    + "         lp.topic,"
    + "         lp.user,"
    + "         lp.postedAt AS lastPostTime FROM"
    + "             ((Forum LEFT JOIN Topic ON Topic.forum = Forum.id)"
    + "             INNER JOIN Post AS lp ON lp.topic = Topic.id)"
    + "             INNER JOIN "
    + "             (SELECT Topic.id AS btid, MAX(lp.postedAt) AS lpt FROM "
    + "             (Forum LEFT JOIN Topic ON Topic.forum = Forum.id)"
    + "             INNER JOIN Post AS lp ON lp.topic = Topic.id "
    + "             GROUP BY Topic.id)"
    + "             ON Topic.id = btid AND lastPostTime = lpt)"
    + "         AS LastPost ON LastPost.topic = Topic.id)"
    + "     LEFT JOIN LikesTopic ON LikesTopic.topic = Topic.id)"
    + "     INNER JOIN Person AS LastPerson ON LastPost.user = LastPerson.id)"
    + "     INNER JOIN Post AS FirstPost ON FirstPost.topic = Topic.id"
    + "     AND FirstPost.postNumber = 1)"
    + "     INNER JOIN Person AS FirstPerson "
    + "     ON FirstPost.user = FirstPerson.id)"
    + " ON Topic.forum = Forum.id"
    + " GROUP BY Topic.id"
    + " HAVING forum.id = ? "
    + " ORDER BY lastPostTime DESC;";

    private AdvancedForumView forum;
    private List<TopicSummaryView> topics;
    private Connection c;
    private Long forumId;
    private String forumTitle;

    public GetAdvancedForum(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        topics = new ArrayList<TopicSummaryView>();
        this.c = c;
    }

    public Result<AdvancedForumView> run(long id) {
        try (PreparedStatement p = c.prepareStatement(statement)) {
            p.setLong(1,id);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    getTopic(r);
                }
                forum = new AdvancedForumView(forumId, forumTitle, topics);
                return Result.success(forum);
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private void getTopic(ResultSet r) throws SQLException {
        forumId                = r.getLong("fid");
        Long topicId           = r.getLong("tid");
        forumTitle             = r.getString("fTitle");
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
            topics.add(topic);
        }

    }
}
