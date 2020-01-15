package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticlesCategoriesDAO extends DAO {

    private static final int LIMIT_FOR_OUTPUT_FOR_SEARCH_BY_CATEGORY_ID = 5;
    private static final String ALL_TITLE_OF_ARTICLE_BY_CATEGORY_ID =
            "SELECT a.id, a.title, a.date_published " +
                    "FROM articles AS a " +
                    "JOIN articles_categories AS ac ON a.id = ac.article_id " +
                    "JOIN categories AS c ON c.id = ac.category_id " +
                    "WHERE category_id = ? LIMIT " + LIMIT_FOR_OUTPUT_FOR_SEARCH_BY_CATEGORY_ID + ";";
    private static final String ALL_CATEGORIES_BY_ARTICLE_ID =
            "SELECT c.id, c.category_name " +
                    "FROM categories AS c " +
                    "JOIN articles_categories AS ac ON c.id = ac.category_id " +
                    "JOIN articles AS a ON a.id = ac.article_id " +
                    "WHERE article_id = ?;";

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
