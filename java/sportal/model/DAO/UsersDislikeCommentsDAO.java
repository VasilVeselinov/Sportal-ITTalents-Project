package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.DAO.interfaceDAO.IDAODeleteFromThirdTable;
import sportal.model.DAO.interfaceDAO.IDAOExistsInThirdTable;
import sportal.model.DAO.interfaceDAO.IDAOManyToMany;

import java.sql.SQLException;

@Component
public class UsersDislikeCommentsDAO extends DAO
        implements
        IDAOManyToMany,
        IDAODeleteFromThirdTable,
        IDAOExistsInThirdTable {

    private static final String INSERT_DISLIKE =
            "INSERT INTO users_disliked_comments (comment_id, user_id) VALUE (?, ?);";
    private static final String DELETE_DISLIKE =
            "DELETE FROM users_disliked_comments " +
                    "WHERE comment_id = ? AND user_id = ?;";
    private static final String CHECK_EXISTS_DISLIKES =
            "SELECT comment_id, user_id " +
                    "FROM users_disliked_comments " +
                    "WHERE comment_id = ? AND user_id = ?;";

    @Override
    public int addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(INSERT_DISLIKE, leftColumn, rightColumn);
    }

    @Override
    public int deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
       return this.jdbcTemplate.update(DELETE_DISLIKE, leftColumn, rightColumn);
    }

    @Override
    public boolean existsInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(CHECK_EXISTS_DISLIKES, leftColumn, rightColumn);
        return rowSet.next();
    }
}
