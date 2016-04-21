package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import uk.ac.bris.cs.databases.api.APIProvider;
import uk.ac.bris.cs.databases.api.AdvancedForumSummaryView;
import uk.ac.bris.cs.databases.api.AdvancedForumView;
import uk.ac.bris.cs.databases.api.ForumSummaryView;
import uk.ac.bris.cs.databases.api.ForumView;
import uk.ac.bris.cs.databases.api.AdvancedPersonView;
import uk.ac.bris.cs.databases.api.PostView;
import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.PersonView;
import uk.ac.bris.cs.databases.api.SimpleForumSummaryView;
import uk.ac.bris.cs.databases.api.SimpleTopicView;
import uk.ac.bris.cs.databases.api.SimplePostView;
import uk.ac.bris.cs.databases.api.TopicView;

/**
 *
 * @author csxdb
 */
public class API implements APIProvider {

    private final Connection c;

    public API(Connection c) {
        this.c = c;
    }

    @Override
    public Result<Map<String, String>> getUsers() {
        final String statement = "SELECT * FROM Person";
        Map<String, String> users = new HashMap<>();

        try(PreparedStatement p = c.prepareStatement(statement)) {
            ResultSet results = p.executeQuery();
            while (results.next()) {
                String username = results.getString("username");
                String name = results.getString("name");

                users.put(username, name);
            }
            results.close();
            return Result.success(users);
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    @Override
    public Result<PersonView> getPersonView(String username) {
        final String statement = "SELECT * FROM Person WHERE username = ?";

        try(PreparedStatement p = c.prepareStatement(statement)) {
            p.setString(1, username);
			
            ResultSet results = p.executeQuery();
            if (results.next()) {
                String name = results.getString("name");
                String studentId = results.getString("stuId");
                PersonView pv = new PersonView(name, username, studentId);
				
                results.close();
                return Result.success(pv);
            } else {
                return Result.failure("Person does not exist!");
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    @Override
    public Result<List<SimpleForumSummaryView>> getSimpleForums() {
        final String statement = "SELECT * FROM Forum ORDER BY title";
        List<SimpleForumSummaryView> forums = new ArrayList<>();

        try(PreparedStatement p = c.prepareStatement(statement)) {
            ResultSet results = p.executeQuery();
            while (results.next()) {
                long id = results.getLong("id");
                String title = results.getString("title");
                SimpleForumSummaryView forum = new SimpleForumSummaryView(id, title);
                forums.add(forum);
            }
            results.close();
            return Result.success(forums);
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    @Override
    public Result<Integer> countPostsInTopic(long topicId) {
        final String statement = "SELECT COUNT(*) AS cnt FROM Post WHERE Topic = ?";

        try(PreparedStatement p = c.prepareStatement(statement)) {
            p.setLong(1, topicId);
			
            ResultSet results = p.executeQuery();
            if (results.next()) {
                int numRows = results.getInt("cnt");
				
                results.close();
				if (numRows > 0) {
					return Result.success(numRows);
				}
            }
            return Result.failure("There is no topic with that ID");
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    // A valid topic with no likes will return 1 row, with Topic.id,
    // and null values for the other cols.
    // An invalid topic will return no rows.
    @Override
    public Result<List<PersonView>> getLikers(long topicId) {
      List<PersonView> pvList = new ArrayList<PersonView>();
      final String SQL = " SELECT Topic.id, name, username, stuId"
                       + " FROM Topic LEFT JOIN"
                       + " (LikesTopic INNER JOIN Person"
                       + " ON LikesTopic.User = Person.id)"
                       + " ON Topic.id = topic"
                       + " WHERE Topic.id = ? "
                       + " ORDER BY name ;";

      try (PreparedStatement p = c.prepareStatement(SQL)) {
        p.setLong(1, topicId);
        ResultSet r = p.executeQuery();

        if (!r.isBeforeFirst() ) {
          return Result.failure("No such topic.");
        }

        while (r.next()) {
          String stuId = r.getString("stuId");
          String name = r.getString("name");
          String username = r.getString("username");
          if (!r.wasNull()) {
            PersonView pv = new PersonView(name, username, stuId);
            pvList.add(pv);
          }
        }
        return Result.success(pvList);
      } catch (SQLException e) {
        return Result.fatal(e.getMessage());
      }
    }

    @Override
    public Result<SimpleTopicView> getSimpleTopic(long topicId) {
      final String SQL = " SELECT Topic.id, title, Post.user, Post.body,"
                       + " postedAt, postNumber"
                       + " FROM Topic LEFT JOIN Post"
                       + " ON Post.topic = Topic.id"
                       + " WHERE Topic.id = ? ;";

      try (PreparedStatement p = c.prepareStatement(SQL)) {
        p.setLong(1, topicId);
        ResultSet r = p.executeQuery();

        if (!r.isBeforeFirst() ) {
          return Result.failure("No such topic.");
        }

        List<SimplePostView> postList = new ArrayList<SimplePostView>();
        String title = "";

        while (r.next()) {
          Integer postedAt = r.getInt("postedAt");
          Integer postNumber = r.getInt("postNumber");
          String user = r.getString("user");
          String body = r.getString("body");
          SimplePostView pview = new SimplePostView(postNumber, user, body,
                                                    postedAt);
          postList.add(pview);
          title = r.getString("title");
        }
        SimpleTopicView tview = new SimpleTopicView(topicId, title, postList);
        return Result.success(tview);
      } catch (SQLException e) {
        return Result.fatal(e.getMessage());
      }
    }

    @Override
    public Result<PostView> getLatestPost(long topicId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result<List<ForumSummaryView>> getForums() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result createForum(String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result createPost(long topicId, String username, String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result addNewPerson(String name, String username, String studentId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result<ForumView> getForum(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result<TopicView> getTopic(long topicId, int page) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result likeTopic(String username, long topicId, boolean like) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result favouriteTopic(String username, long topicId, boolean fav) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result createTopic(long forumId, String username, String title, String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result<List<AdvancedForumSummaryView>> getAdvancedForums() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result<AdvancedPersonView> getAdvancedPersonView(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result<AdvancedForumView> getAdvancedForum(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Result likePost(String username, long topicId, int post, boolean like) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   }
