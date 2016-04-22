package uk.ac.bris.cs.databases.cwk3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.bris.cs.databases.api.Result;
import uk.ac.bris.cs.databases.api.PersonView;

import java.util.List;
import java.util.ArrayList;

class GetLikers {
    private Connection c;
    private List<PersonView> pvList;
    private final String SQL = " SELECT Topic.id, name, username, stuId"
                             + " FROM Topic LEFT JOIN"
                             + " (LikesTopic INNER JOIN Person"
                             + " ON LikesTopic.User = Person.id)"
                             + " ON Topic.id = topic"
                             + " WHERE Topic.id = ? ORDER BY name ;";

    public GetLikers(Connection c) {
        if (c == null) { throw new IllegalStateException(); }
        pvList = new ArrayList<PersonView>();
        this.c = c;
    }

    public Result<List<PersonView>> run(long topicId) {

        try (PreparedStatement p = c.prepareStatement(SQL)) {
            p.setLong(1, topicId);
            try (ResultSet r = p.executeQuery()) {
                if (!r.isBeforeFirst() ) {
                    return Result.failure("No such topic.");
                }
                while (r.next()) {
                    readLiker(r);
                }
                return Result.success(pvList);
            }
        } catch (SQLException e) {
            return Result.fatal(e.getMessage());
        }
    }

    private void readLiker(ResultSet r) throws SQLException {
        String stuId = r.getString("stuId");
        String name = r.getString("name");
        String username = r.getString("username");
        if (!r.wasNull()) {
            PersonView pv = new PersonView(name, username, stuId);
            pvList.add(pv);
        }
    }
}
