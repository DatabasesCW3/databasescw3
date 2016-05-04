package uk.ac.bris.cs.databases.cwk3;

import java.sql.*;
import java.util.Date;
import uk.ac.bris.cs.databases.api.Result;

public class FavouriteTopic {
    private Connection c;
    private final String userAndTopicCheck = "SELECT * FROM Person LEFT JOIN Topic WHERE Person.username = ? AND Topic.id = ?";
    private final String statement = "SELECT * FROM Favourite WHERE user = ? AND topic = ?";
    private final String makeFavourite = "INSERT INTO Favourite VALUES (null, ?, ?)";
    private final String removeFavourite = "SELECT * FROM Favourite WHERE user = ? AND topic = ?";
    private Long userID;

    public FavouriteTopic(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        this.c = c;
    }

    public Result run(String username, long topicId, boolean fav) {
        if (!validate(username, topicId))
            return Result.failure("Invalid input");

        try {
            userID = checkForExistingUserAndTopicAndReturnUserID(username, topicId);
            if (userID == null)
                return Result.failure("User does not exist!");

            boolean alreadyFavourited = alreadyFavourited();

            if (alreadyFavourited && !fav) {
                return updateFavourite(makeFavourite, topicId);
            } else if (!alreadyFavourited && fav){
                return updateFavourite(removeFavourite, topicId);
            }
        }
        catch (SQLException e) {
            Result.fatal(e.getMessage());
        }

        return Result.success();
    }

    private Result updateFavourite(String operation, long topicID) {
        try(PreparedStatement p = c.prepareStatement(operation)) {
            p.setLong(1, userID);
            p.setLong(2, topicID);
            p.execute();
            c.commit();
            return Result.success();
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private boolean alreadyFavourited() {
        try (PreparedStatement p = c.prepareStatement(statement)){
            ResultSet results = p.executeQuery();
            return (results.isBeforeFirst());
        } catch (SQLException e) {
            Result.fatal(e.getMessage());
        }
        return false;
    }

    private Long checkForExistingUserAndTopicAndReturnUserID(String username, long topicID) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(userAndTopicCheck)) {

            p.setString(1, username);
            p.setLong(2, topicID);
            ResultSet r = p.executeQuery();

            if (r.isBeforeFirst())
                return r.getLong("id");
            else
                return null;
        }
    }

    private boolean validate(String username, long topicID) {
        if (username == null || "".equals(username)) { return false; }
        if (topicID < 0) { return false;}
        return true;
    }
}
