package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.TopicSummaryView;
import uk.ac.bris.cs.databases.api.AdvancedForumSummaryView;

import java.util.List;
import java.util.ArrayList;

class GetAdvancedForums {
  
    private final String statement = "SELECT"
    + " Forum.id             AS fid,"
    + " Forum.title          AS fTitle,"
    + " Topic.id             AS tid,"
    + " Topic.title          AS tTitle, "
    + " LastPost.postedAt    AS lastPostTime,"
    + " LastPost.postNumber  AS postCount,"
    + " LastPerson.username  AS lastPoster,"
    + " FirstPost.postedAt   AS firstPostTime,"
    + " FirstPerson.username AS firstPoster,"
    + " FirstPerson.name     AS firstPosterName,"
    + " COUNT(LikesTopic.id) AS likes"
    + " FROM"
    + "     Forum LEFT JOIN ("
    + "     ((((Topic "
    + "     INNER JOIN Post AS LastPost ON LastPost.topic = Topic.id"
    + "     AND LastPost.postedAt IN "
    + "         (SELECT LastPost.postedAt AS lastPostTime"
    + "         FROM"
    + "             ((Forum LEFT JOIN Topic ON Topic.forum = Forum.id)"
    + "             INNER JOIN Post AS LastPost ON LastPost.topic = Topic.id)"
    + "             INNER JOIN "
    + "             (SELECT Forum.id AS bfid, "
    + "             MAX(LastPost.postedAt) AS lpt FROM "
    + "             (Forum LEFT JOIN Topic ON Topic.forum = Forum.id)"
    + "             INNER JOIN Post AS LastPost "
    + "             ON LastPost.topic = Topic.id "
    + "             GROUP BY forum.id)"
    + "             ON Forum.id = bfid AND lastPostTime = lpt))"
    + "     INNER JOIN Person AS LastPerson ON LastPost.user = LastPerson.id)"
    + "     INNER JOIN Post AS FirstPost ON FirstPost.topic = Topic.id"
    + "     AND FirstPost.postNumber = 1)"
    + "     INNER JOIN Person AS FirstPerson "
    + "     ON FirstPost.user = FirstPerson.id)"
    + "     LEFT JOIN LikesTopic ON LikesTopic.topic = Topic.id)"
    + " ON Topic.forum = Forum.id"
    + " GROUP BY fid"
    + " ORDER BY fTitle ASC;";
    
    private List<AdvancedForumSummaryView> forums;
    private Connection c;

    public GetAdvancedForums(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
        forums = new ArrayList<AdvancedForumSummaryView>();
    }

    public Result<List<AdvancedForumSummaryView>> run() {
        try (PreparedStatement p = c.prepareStatement(statement)) {
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    addForum(r);
                }
                return Result.success(forums);
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private void addForum(ResultSet r) throws SQLException {
        Long forumId           = r.getLong("fid");
        Long topicId           = r.getLong("tid");
        String forumTitle      = r.getString("fTitle");
        String topicTitle      = r.getString("tTitle");
        int postCount          = r.getInt("postCount");
        int likes              = r.getInt("likes");
        int lastPostTime       = r.getInt("lastPostTime");
        int firstPostTime      = r.getInt("firstPostTime");
        String lastPoster      = r.getString("lastPoster");
        String firstPoster     = r.getString("firstPoster");
        String firstPosterName = r.getString("firstPosterName");
        
        TopicSummaryView latestTopic;

        if (r.wasNull()) { latestTopic = null; }
        else { 
            latestTopic = new TopicSummaryView(topicId, 
                                               forumId, 
                                               topicTitle, 
                                               postCount, 
                                               firstPostTime, 
                                               lastPostTime, 
                                               lastPoster,
                                               likes,
                                               firstPoster, 
                                               firstPosterName);
        }
        AdvancedForumSummaryView forum = 
            new AdvancedForumSummaryView(forumId,
                                         forumTitle,
                                         latestTopic);
        forums.add(forum);
    }
}
