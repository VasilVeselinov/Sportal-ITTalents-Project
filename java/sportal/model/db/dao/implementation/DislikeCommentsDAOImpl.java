package sportal.model.db.dao.implementation;

import sportal.annotations.DAOAnnotation;
import sportal.model.db.dao.DAO;
import sportal.model.db.dao.IDislikeCommentsDAO;

import java.sql.SQLException;

@DAOAnnotation
public class DislikeCommentsDAOImpl extends DAO implements IDislikeCommentsDAO {

    private static final String ADD_DISLIKE =
            "INSERT INTO users_disliked_comments (comment_id, user_id) " +
                    "VALUE (?, ?);";
    private static final String DELETE_DISLIKE =
            "DELETE FROM users_disliked_comments " +
                    "WHERE comment_id = ? AND user_id = ?;";

    @Override
    public void add(long leftColumn, long rightColumn) throws SQLException {
        this.jdbcTemplate.update(ADD_DISLIKE, leftColumn, rightColumn);
    }

    @Override
    public int delete(long leftColumn, long rightColumn) {
        return this.jdbcTemplate.update(DELETE_DISLIKE, leftColumn, rightColumn);
    }
}
