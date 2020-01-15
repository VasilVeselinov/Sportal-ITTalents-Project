package sportal.model.dao;

import org.springframework.stereotype.Component;
import sportal.model.dao.interfaceDAO.IDAODeleteFromThirdTable;
import sportal.model.dao.interfaceDAO.IDAOManyToMany;

import java.sql.SQLException;

@Component
public class UsersDislikeCommentsDAO extends DAO
        implements
        IDAOManyToMany,
        IDAODeleteFromThirdTable {

    private static final String ADD_DISLIKE_BY_COMMENT_ID_AND_USER_ID =
            "INSERT INTO users_disliked_comments (comment_id, user_id) VALUE (?, ?);";
    private static final String DELETE_DISLIKE_BY_COMMENT_ID_AND_USER_ID =
            "DELETE FROM users_disliked_comments " +
                    "WHERE comment_id = ? AND user_id = ?;";

    @Override
    public int addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(ADD_DISLIKE_BY_COMMENT_ID_AND_USER_ID, leftColumn, rightColumn);
    }

    @Override
    public int deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
       return this.jdbcTemplate.update(DELETE_DISLIKE_BY_COMMENT_ID_AND_USER_ID, leftColumn, rightColumn);
    }
}
