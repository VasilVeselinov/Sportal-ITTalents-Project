package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.dao.interfaceDAO.IDAODeleteFromThirdTable;
import sportal.model.dao.interfaceDAO.IDAOManyToMany;

import java.sql.SQLException;

@Component
public class UsersLikeArticlesDAO extends DAO
        implements IDAOManyToMany, IDAODeleteFromThirdTable {

    private static final String CHECK_EXISTS_LIKE_BY_ARTICLE_ID_AND_USER_ID =
            "SELECT article_id, user_id " +
                    "FROM users_like_articles " +
                    "WHERE article_id = ? AND user_id = ?";
    private static final String DELETE_LIKE_BY_ARTICLE_ID_AND_USER_ID =
            "DELETE FROM users_like_articles WHERE article_id = ? AND user_id = ?;";
    private static final String ADD_LIKE_BY_ARTICLE_ID_AND_USER_ID =
            "INSERT INTO users_like_articles (article_id, user_id) VALUE (?, ?);";

    @Override
    public int addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(ADD_LIKE_BY_ARTICLE_ID_AND_USER_ID, leftColumn, rightColumn);
    }

    @Override
    public int deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(DELETE_LIKE_BY_ARTICLE_ID_AND_USER_ID, leftColumn, rightColumn);
    }

    public boolean existsLikeByArticleIdAndUserId(long articleId, long userId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(
                CHECK_EXISTS_LIKE_BY_ARTICLE_ID_AND_USER_ID, articleId, userId);
        return rowSet.next();
    }
}
