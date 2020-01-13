package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.DAO.interfaceDAO.IDAODeleteFromThirdTable;
import sportal.model.DAO.interfaceDAO.IDAOExistsInThirdTable;
import sportal.model.DAO.interfaceDAO.IDAOManyToMany;

import java.sql.SQLException;

@Component
public class UsersLikeCommentsDAO extends DAO
        implements
        IDAOManyToMany,
        IDAODeleteFromThirdTable,
        IDAOExistsInThirdTable {

    private static final String INSERT_LIKE = "INSERT INTO users_like_comments (comment_id, user_id) VALUE (?, ?);";
    private static final String DELETE_LIKE = "DELETE FROM users_like_comments WHERE comment_id = ? AND user_id = ?;";
    private static final String CHECK_EXISTS_LIKES =
            "SELECT comment_id, user_id " +
                    "FROM users_like_comments " +
                    "WHERE comment_id = ? AND user_id = ?;";

    @Override
    public int addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(INSERT_LIKE, leftColumn, rightColumn);
    }

    @Override
    public int deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(DELETE_LIKE, leftColumn, rightColumn);
    }

    @Override
    public boolean existsInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(CHECK_EXISTS_LIKES, leftColumn, rightColumn);
        return rowSet.next();
    }
}
