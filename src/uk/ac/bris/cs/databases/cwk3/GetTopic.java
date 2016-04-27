package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.TopicView;
import uk.ac.bris.cs.databases.api.PostView;

import java.util.List;
import java.util.ArrayList;

class GetTopic {
    private Connection c;
    private final List<PostView> postList;
    private Long fid;
    private Long tid;
    private String forumTitle;
    private String topicTitle;
    private final String SQL = "SELECT"
                            + " postNumber, name, username, body, postedAt,"
                            + " Forum.id AS fid, Forum.title AS fTitle,"
                            + " Topic.id AS tid, Topic.title AS tTitle,"
                            + " COUNT(LikesPost.id) AS likes"
                            + " FROM (Post INNER JOIN Person "
                            + " ON Post.user = Person.id"
                            + " INNER JOIN Topic ON Post.topic = Topic.id"
                            + " INNER JOIN Forum on Topic.forum = forum.id)"
                            + " LEFT JOIN LikesPost ON Post.id = LikesPost.post"
                            + " WHERE Post.topic = ? "
                            + " GROUP BY Post.id"
                            + " ORDER BY postedAt"
                            + " LIMIT ? OFFSET ? ;";

    public GetTopic(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        postList = new ArrayList<PostView>();
        this.c = c;
    }

    public Result<TopicView> run(long topicId, int page) {
        try (PreparedStatement p = c.prepareStatement(SQL)) {
            p.setLong(1, topicId);
            p.setLong(2, calculateLimit(page));
            p.setLong(3, calculatePages(page));
            try (ResultSet r = p.executeQuery()) {
                if (!r.isBeforeFirst() ) {
                    return Result.failure("No such set of posts.");
                }
                getTopicFields(r);
                getPosts(r);
                TopicView tview = new TopicView(fid, tid, forumTitle,
                                                topicTitle, postList, page);
                return Result.success(tview);
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private int calculatePages(int page) {
        if (page > 0) { return 10 * (page - 1); }
        else { return 0; }
    }

    private int calculateLimit(int page) {
        if (page > 0) { return 10; }
        else { return -1; } //unlimited
    }

    private void getPosts(ResultSet r) throws SQLException {
        do {

            Integer postedAt = r.getInt("postedAt");
            Integer postNumber = r.getInt("postNumber");
            Integer likes = r.getInt("likes");
            String name = r.getString("name");
            String username = r.getString("username");
            String body = r.getString("body");
            Long fid = r.getLong("fid");
            Long tid = r.getLong("tid");
            PostView pview = new PostView(fid, tid, postNumber, name,
                                          username, body, postedAt, likes);
            postList.add(pview);
        } while (r.next());
    }

    private void getTopicFields(ResultSet r) throws SQLException {
        r.next();
        fid = r.getLong("fid");
        tid = r.getLong("tid");
        forumTitle = r.getString("fTitle");
        topicTitle = r.getString("tTitle");
    }
}
