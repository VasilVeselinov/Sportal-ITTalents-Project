package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.dao.interfaceDAO.IDAODeleteFromThirdTable;
import sportal.model.dao.interfaceDAO.IDAOExistsInThirdTable;
import sportal.model.dao.interfaceDAO.IDAOManyToMany;

import java.sql.SQLException;

@Component
public class UsersLikeArticlesDAO extends DAO
        implements IDAOManyToMany, IDAODeleteFromThirdTable, IDAOExistsInThirdTable {

    private static final String CHECK_EXISTS_LIKE =
            "SELECT article_id, user_id " +
                    "FROM users_like_articles " +
                    "WHERE article_id = ? AND user_id = ?";
    private static final String COUNT_LIKES =
            "SELECT COUNT(user_id) AS number_likes " +
                    "FROM users_like_articles " +
                    "WHERE article_id = ?;";
    private static final String DELETE_LIKE = "DELETE FROM users_like_articles WHERE article_id = ? AND user_id = ?;";
    private static final String INSERT_LIKE = "INSERT INTO users_like_articles (article_id, user_id) VALUE (?, ?);";

    @Override
    public int addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(INSERT_LIKE, leftColumn, rightColumn);
    }

    @Override
    public int deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException {
        return this.jdbcTemplate.update(DELETE_LIKE, leftColumn, rightColumn);
    }

    public int totalLikesByArticleId(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(COUNT_LIKES, id);
        if (rowSet.next()) {
            return rowSet.getInt("number_likes");
        }
        return 0;
    }

    @Override
    public boolean existsInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(CHECK_EXISTS_LIKE, leftColumn, rightColumn);
        return rowSet.next();
    }
}
