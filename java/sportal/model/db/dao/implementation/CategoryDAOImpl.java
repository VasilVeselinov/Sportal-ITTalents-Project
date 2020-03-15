package sportal.model.db.dao.implementation;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import sportal.annotations.DAOAnnotation;
import sportal.model.db.dao.DAO;
import sportal.model.db.dao.ICategoryDAO;
import sportal.model.db.pojo.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@DAOAnnotation
public class CategoryDAOImpl extends DAO implements ICategoryDAO {

    private static final String ARTICLE_CATEGORIES =
            "SELECT c.id, c.category_name " +
                    "FROM categories AS c " +
                    "JOIN articles_categories AS ac ON c.id = ac.category_id " +
                    "JOIN articles AS a ON a.id = ac.article_id " +
                    "WHERE article_id = ?;";
    private static final String EXISTS_COMBINATION =
            "SELECT article_id, category_id " +
                    "FROM articles_categories " +
                    "WHERE article_id = ? AND category_id = ?;";
    private static final String ADD_CATEGORY_BY_ARTICLE_ID =
            "INSERT INTO articles_categories (article_id, category_id) " +
                    "VALUE (?, ?);";
    private static final String DELETE_CATEGORY_FROM_ARTICLE =
            "DELETE FROM articles_categories WHERE article_id = ? AND category_id = ?;";

    @Override
    public List<Category> findAllByArticleId(long articleID) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ARTICLE_CATEGORIES, articleID);
        List<Category> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createCategoryByRowSet(rowSet));
        }
        return listWithCategories;
    }

    private Category createCategoryByRowSet(SqlRowSet rowSet) {
        Category category = new Category();
        category.setId(rowSet.getLong("id"));
        category.setCategoryName(rowSet.getString("category_name"));
        return category;
    }

    @Override
    public boolean existsCombination(long articleId, long categoryId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(EXISTS_COMBINATION, articleId, categoryId);
        return rowSet.next();
    }

    @Override
    public void add(long leftColumn, long rightColumn) throws SQLException {
        this.jdbcTemplate.update(ADD_CATEGORY_BY_ARTICLE_ID, leftColumn, rightColumn);
    }

    @Override
    public int delete(long leftColumn, long rightColumn) {
        return this.jdbcTemplate.update(DELETE_CATEGORY_FROM_ARTICLE, leftColumn, rightColumn);
    }
}
