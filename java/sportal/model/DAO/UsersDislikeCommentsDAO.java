package sportal.model.dao;

import org.springframework.stereotype.Component;
import sportal.model.dao.interfaceDAO.IDAODeleteFromSupportedTable;
import sportal.model.dao.interfaceDAO.IDAOManyToMany;

import java.sql.SQLException;

@Component
public class UsersDislikeCommentsDAO extends DAO implements IDAOManyToMany, IDAODeleteFromSupportedTable {

    private static final String ADD_DISLIKE_BY_COMMENT_ID_AND_USER_ID =
            "INSERT INTO users_disliked_comments (comment_id, user_id) VALUE (?, ?);";
    private static final String DELETE_DISLIKE =
            "DELETE FROM users_disliked_comments " +
                    "WHERE comment_id = ? AND user_id = ?;";

    @Override
    public void addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        this.jdbcTemplate.update(ADD_DISLIKE_BY_COMMENT_ID_AND_USER_ID, leftColumn, rightColumn);
    }

    @Override
    public int delete(long leftColumn, long rightColumn) {
        return this.jdbcTemplate.update(DELETE_DISLIKE, leftColumn, rightColumn);
    }
}
