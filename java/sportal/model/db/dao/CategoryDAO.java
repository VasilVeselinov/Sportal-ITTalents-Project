package sportal.model.db.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.db.pojo.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDAO extends DAO {

    private static final String ALL_CATEGORIES_BY_ARTICLE_ID =
            "SELECT c.id, c.category_name " +
                    "FROM categories AS c " +
                    "JOIN articles_categories AS ac ON c.id = ac.category_id " +
                    "JOIN articles AS a ON a.id = ac.article_id " +
                    "WHERE article_id = ?;";
    private static final String FIND_COMBINATION =
            "SELECT article_id, category_id " +
                    "FROM articles_categories " +
                    "WHERE article_id = ? AND category_id = ?;";
    private static final String ADD_CATEGORY_BY_ARTICLE_ID =
            "INSERT INTO articles_categories (article_id, category_id) " +
                    "VALUE (?, ?);";
    private static final String DELETE_CATEGORY_FROM_ARTICLE =
            "DELETE FROM articles_categories WHERE article_id = ? AND category_id = ?;";

    public List<Category> allCategoriesByArticlesId(long articleID) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_CATEGORIES_BY_ARTICLE_ID, articleID);
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

    public boolean existsCombination(long articleId, long categoryId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_COMBINATION, articleId, categoryId);
        return rowSet.next();
    }

    public void addCategoryToArticleById(long articleId, long categoryId) throws SQLException {
        this.jdbcTemplate.update(ADD_CATEGORY_BY_ARTICLE_ID, articleId, categoryId);
    }

    public void deleteCategoryFromArticleById(long articleId, long categoryId) {
        this.jdbcTemplate.update(DELETE_CATEGORY_FROM_ARTICLE, articleId, categoryId);
    }
}
