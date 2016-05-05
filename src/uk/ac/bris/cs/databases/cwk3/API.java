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
import uk.ac.bris.cs.databases.api.SimpleTopicSummaryView;
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
        final String statement = "SELECT name, username FROM Person";
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
        final String statement = "SELECT id, title FROM Forum ORDER BY title";

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
    // and null values for the other cols. An invalid topic will return no rows.
    @Override
    public Result<List<PersonView>> getLikers(long topicId) {
        GetLikers gl = new GetLikers(c);
        return gl.run(topicId);
    }

    @Override
    public Result<SimpleTopicView> getSimpleTopic(long topicId) {
        GetSimpleTopic gst = new GetSimpleTopic(c);
        return gst.run(topicId);
    }

    @Override
    public Result<PostView> getLatestPost(long topicId) {
        GetLatestPost glp = new GetLatestPost(c);
        return glp.run(topicId);
    }

    @Override
    public Result<List<ForumSummaryView>> getForums(){
        GetForums gf = new GetForums(c);
        return gf.run();
    }

    @Override
    public Result createForum(String title) {
        final String checkStatement = "SELECT * FROM Forum WHERE title = ?";

		if (title == null || title.equals("")) {
			return Result.failure("Forum name must not be null or empty!");
		}

        try(PreparedStatement p = c.prepareStatement(checkStatement)) {
            p.setString(1, title);

            ResultSet results = p.executeQuery();
            if (results.next()) {
                return Result.failure("Forum with same name already exists!");
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }

        final String insertStatement = "INSERT INTO Forum(title) VALUES (?)";

		try(PreparedStatement p = c.prepareStatement(insertStatement)) {
            p.setString(1, title);
            p.executeUpdate();
            c.commit();
			return Result.success();
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    @Override
    public Result createPost(long topicId, String username, String text) {
//        throw new UnsupportedOperationException("Not supported yet.");
        CreatePost cp = new CreatePost(c);
        return cp.run(topicId, username, text);
    }

    @Override
    public Result addNewPerson(String name, String username, String studentId) {
        AddNewPerson anp = new AddNewPerson(c);
        return anp.run(name, username, studentId);
    }

    @Override
    public Result<ForumView> getForum(long id) {
        final String statement = "SELECT Forum.title AS fTitle, Topic.id AS tId, Topic.title AS tTitle FROM Forum LEFT JOIN Topic ON Forum.id = Topic.forum WHERE Forum.id = ?";
		String forumTitle = null;
		List<SimpleTopicSummaryView> topics = new ArrayList<SimpleTopicSummaryView>();

        try(PreparedStatement p = c.prepareStatement(statement)) {
            p.setLong(1, id);

			try (ResultSet r = p.executeQuery()) {
				if (!r.isBeforeFirst()) {
					return Result.failure("Forum does not exist!");
				}

                while (r.next()) {
					Long topicId = r.getLong("tId");
					forumTitle = r.getString("fTitle");
					String topicTitle = r.getString("tTitle");

					if (!r.wasNull()) {
						SimpleTopicSummaryView topic = new SimpleTopicSummaryView(topicId, id, topicTitle);
						topics.add(topic);
					}
                }
            }

			ForumView forum = new ForumView(id, forumTitle, topics);
			return Result.success(forum);
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    @Override
    public Result<TopicView> getTopic(long topicId, int page) {
        GetTopic gt = new GetTopic(c);
        return gt.run(topicId, page);    }

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
        CreateTopic ct = new CreateTopic(c);
        return ct.run(forumId, username, title, text);
    }

    @Override
    public Result<List<AdvancedForumSummaryView>> getAdvancedForums() {
        GetAdvancedForums gaf = new GetAdvancedForums(c);
        return gaf.run();
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
