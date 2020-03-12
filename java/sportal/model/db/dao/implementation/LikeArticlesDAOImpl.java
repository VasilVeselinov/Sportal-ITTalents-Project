package sportal.model.db.dao.implementation;

import sportal.annotations.DAOAnnotation;
import sportal.model.db.dao.DAO;
import sportal.model.db.dao.ILikeArticlesDAO;

import java.sql.SQLException;

@DAOAnnotation
public class LikeArticlesDAOImpl extends DAO implements ILikeArticlesDAO {

    private static final String ADD_LIKE =
            "INSERT INTO users_like_articles (article_id, user_id) " +
                    "VALUE (?, ?);";
    private static final String DELETE_LIKE =
            "DELETE FROM users_like_articles " +
                    "WHERE article_id = ? AND user_id = ?;";

    @Override
    public void add(long leftColumn, long rightColumn) throws SQLException {
        this.jdbcTemplate.update(ADD_LIKE, leftColumn, rightColumn);
    }

    @Override
    public int delete(long leftColumn, long rightColumn) {
        return this.jdbcTemplate.update(DELETE_LIKE, leftColumn, rightColumn);
    }
}
