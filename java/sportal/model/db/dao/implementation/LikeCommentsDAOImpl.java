package sportal.model.db.dao.implementation;

import sportal.annotations.DAOAnnotation;
import sportal.model.db.dao.DAO;
import sportal.model.db.dao.ILikeCommentsDAO;

import java.sql.SQLException;

@DAOAnnotation
public class LikeCommentsDAOImpl extends DAO implements ILikeCommentsDAO {

    private static final String ADD_LIKE =
            "INSERT INTO users_like_comments (comment_id, user_id) " +
                    "VALUE (?, ?);";
    private static final String DELETE_LIKE =
            "DELETE FROM users_like_comments " +
                    "WHERE comment_id = ? AND user_id = ?;";

    @Override
    public void add(long leftColumn, long rightColumn) throws SQLException {
        this.jdbcTemplate.update(ADD_LIKE, leftColumn, rightColumn);
    }

    @Override
    public int delete(long leftColumn, long rightColumn) {
        return this.jdbcTemplate.update(DELETE_LIKE, leftColumn, rightColumn);
    }
}
