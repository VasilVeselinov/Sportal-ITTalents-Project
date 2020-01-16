package sportal.model.dao;

import org.springframework.stereotype.Component;
import sportal.model.dao.interfaceDAO.IDAODeleteFromSupportedTable;
import sportal.model.dao.interfaceDAO.IDAOManyToMany;

import java.sql.SQLException;

@Component
public class UsersLikeCommentsDAO extends DAO
        implements
        IDAOManyToMany,
        IDAODeleteFromSupportedTable {

    private static final String INSERT_LIKE = "INSERT INTO users_like_comments (comment_id, user_id) VALUE (?, ?);";
    private static final String DELETE_LIKE = "DELETE FROM users_like_comments WHERE comment_id = ? AND user_id = ?;";

    @Override
    public int addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(INSERT_LIKE, leftColumn, rightColumn);
    }

    @Override
    public int delete(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(DELETE_LIKE, leftColumn, rightColumn);
    }
}
