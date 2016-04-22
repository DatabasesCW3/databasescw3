package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.SimpleTopicView;
import uk.ac.bris.cs.databases.api.SimplePostView;

import java.util.List;
import java.util.ArrayList;

class GetSimpleTopic {
    private Connection c;
    List<SimplePostView> postList;
    String title = "";
    private final String SQL = " SELECT Topic.id, title, Post.user, Post.body,"
                     + " postedAt, postNumber"
                     + " FROM Topic LEFT JOIN Post"
                     + " ON Post.topic = Topic.id"
                     + " WHERE Topic.id = ? ;";

    public GetSimpleTopic(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        postList = new ArrayList<SimplePostView>();
        this.c = c;
    }

    public Result<SimpleTopicView> run(long topicId) {
        try (PreparedStatement p = c.prepareStatement(SQL)) {
            p.setLong(1, topicId);
            try (ResultSet r = p.executeQuery()) {
                if (!r.isBeforeFirst() ) {
                    return Result.failure("No such topic.");
                }
                while (r.next()) {
                    addPost(r);
                }
                SimpleTopicView tview = new SimpleTopicView(topicId, title,
                                                            postList);
                return Result.success(tview);
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private void addPost(ResultSet r) throws SQLException {

        if (title.equals("")) {
            title = r.getString("title");
        }
        Integer postedAt = r.getInt("postedAt");
        Integer postNumber = r.getInt("postNumber");
        String user = r.getString("user");
        String body = r.getString("body");
        SimplePostView pview = new SimplePostView(postNumber, user, body,
                                                  postedAt);
        postList.add(pview);
    }
}
