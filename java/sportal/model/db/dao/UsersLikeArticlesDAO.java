package sportal.model.db.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.db.dao.interfaceDAO.IDAODeleteFromSupportedTable;
import sportal.model.db.dao.interfaceDAO.IDAOManyToMany;
import sportal.model.db.pojo.ExistsObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsersLikeArticlesDAO extends DAO implements IDAOManyToMany, IDAODeleteFromSupportedTable {

    private static final String FIND_COMBINATION_AND_ARTICLE =
            "SELECT ula.article_id, ula.user_id, a.id AS a_id " +
                    "FROM users_like_articles AS ula " +
                    "RIGHT JOIN articles AS a ON ula.article_id = a.id " +
                    "WHERE a.id = ?";
    private static final String ADD_LIKE_BY_ARTICLE_ID_AND_USER_ID =
            "INSERT INTO users_like_articles (article_id, user_id) VALUE (?, ?);";
    private static final String DELETE_LIKE_BY_ARTICLE_ID_AND_USER_ID =
            "DELETE FROM users_like_articles WHERE article_id = ? AND user_id = ?;";

    @Override
    public void addInThirdTable(long leftColumn, long rightColumn) throws SQLException {
        this.jdbcTemplate.update(ADD_LIKE_BY_ARTICLE_ID_AND_USER_ID, leftColumn, rightColumn);
    }

    @Override
    public int delete(long leftColumn, long rightColumn) {
        return this.jdbcTemplate.update(DELETE_LIKE_BY_ARTICLE_ID_AND_USER_ID, leftColumn, rightColumn);
    }

    public List<ExistsObject> existsCombinationAndArticleId(long articleId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_COMBINATION_AND_ARTICLE, articleId);
        List<ExistsObject> objects = new ArrayList<>();
        while (rowSet.next()) {
            ExistsObject existsObj = new ExistsObject();
            existsObj.setLeftColumnId(rowSet.getLong("article_id"));
            existsObj.setRightColumnId(rowSet.getLong("user_id"));
            existsObj.setLeftId(rowSet.getLong("a_id"));
            objects.add(existsObj);
        }
        return objects;
    }
}
