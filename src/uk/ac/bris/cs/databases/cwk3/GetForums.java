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

class GetForums {
    private final String statement = " SELECT max(postedAt) pa,"
                                   + " Forum.id AS fid, "
                                   + " Forum.title AS fTitle,"
                                   + " Topic.id AS tid,"
                                   + " Topic.title AS tTitle"
                                   + " FROM (Forum LEFT JOIN Topic"
                                   + " ON Forum.id = Topic.forum)"
                                   + " LEFT JOIN Post ON Topic.id = Post.topic"
                                   + " GROUP BY Forum.id"
                                   + " ORDER BY Forum.title ASC;";

    private List<ForumSummaryView> forums;
    private Connection c;

    public GetForums(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
        forums = new ArrayList<ForumSummaryView>();
    }

    public Result<List<ForumSummaryView>> run() {
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
        Long forumId = r.getLong("fid");
        Long topicId = r.getLong("tid");
        String forumTitle = r.getString("fTitle");
        String topicTitle = r.getString("tTitle");

        SimpleTopicSummaryView latestTopic;

        if (r.wasNull()) { latestTopic = null; }
        else { latestTopic = new SimpleTopicSummaryView(topicId, forumId,
                                                        topicTitle);
        }
        ForumSummaryView forum = new ForumSummaryView(forumId, forumTitle,
                                                      latestTopic);
        forums.add(forum);
    }
}
