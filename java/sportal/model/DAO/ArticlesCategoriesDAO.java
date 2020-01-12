package sportal.model.DAO;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.exception.TransactionException;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticlesCategoriesDAO extends DAO {


    private static final String INSERT_CATEGORIES_TO_ARTICLE =
            "INSERT INTO articles_categories (category_id, article_id) VALUES (?, ?);";
    private static final String ALL_TITLE_OF_ARTICLE_BY_CATEGORY_ID =
            "SELECT a.id, a.title, a.date_published " +
            "FROM articles AS a " +
            "JOIN articles_categories AS ac ON a.id = ac.article_id " +
            "JOIN categories AS c ON c.id = ac.category_id " +
            "WHERE category_id = ? LIMIT 10;";
    private static final String ALL_CATEGORIES_BY_ARTICLE_ID =
            "SELECT c.id, c.category_name " +
            "FROM categories AS c " +
            "JOIN articles_categories AS ac ON c.id = ac.category_id " +
            "JOIN articles AS a ON a.id = ac.article_id " +
            "WHERE article_id = ?;";

    public void addListFromCategoriesToArticleId(List<Category> categoryList, long articleId) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CATEGORIES_TO_ARTICLE)) {
            connection.setAutoCommit(false);
            for (Category category : categoryList) {
                statement.setLong(1, category.getId());
                statement.setLong(2, articleId);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new TransactionException(UNSUCCESSFUL_TRANSACTION);
            } catch (SQLException ex) {
                throw new SQLException(UNSUCCESSFUL_CONNECTION_ROLLBACK);
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Article> allArticlesByCategoryId(long categoryID) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_TITLE_OF_ARTICLE_BY_CATEGORY_ID, categoryID);
        List<Article> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createArticleByRowSet(rowSet));
        }

        return listWithCategories;
    }

    private Article createArticleByRowSet(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getInt("id"));
        article.setTitle(rowSet.getString("title"));
        article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
        return article;
    }

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
}
