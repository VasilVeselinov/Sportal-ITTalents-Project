package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.exception.TransactionException;
import sportal.model.dao.interfaceDAO.IDAODeleteById;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;
import sportal.model.pojo.Picture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleDAO extends DAO implements IDAODeleteById {

    private static final String ADD_NEW_ARTICLE =
            "INSERT INTO articles (title ,full_text, date_published, views, author_id) " +
                    "VALUES (?, ?, ?, ?, ?);";
    private static final String SET_ARTICLE_ID_INTO_PICTURES_TABLE =
            "UPDATE pictures SET article_id = ? WHERE id = ?;";
    private static final String ADD_CATEGORIES_TO_ARTICLE =
            "INSERT INTO articles_categories (category_id, article_id) VALUES (?, ?);";
    private static final String TOP_FIVE_MOST_VIEWED_ARTICLE =
            "SELECT id, title, date_published, views " +
                    "FROM articles " +
                    "WHERE DATE(date_published) = CURRENT_DATE() " +
                    "ORDER BY views DESC LIMIT 5;";
    private static final String UPDATE_ARTICLE_TITLE_AND_TEXT =
            "UPDATE articles SET title = ?, full_text = ? WHERE id = ?;";
    private static final String UPDATE_VIEWS_BY_ARTICLE_ID = "UPDATE articles SET views = views + 1 WHERE id = ?;";
    private static final String DELETE_ARTICLE = "DELETE FROM articles WHERE id = ?;";
    private static final String FIND_ARTICLE_BY_ID =
            "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id, " +
                    "u.user_name, COUNT(ula.user_id) AS number_of_likes " +
                    "FROM articles AS a " +
                    "LEFT JOIN users AS u ON a.author_id = u.id " +
                    "LEFT JOIN users_like_articles AS ula ON ula.article_id = a.id " +
                    "WHERE a.id = ?;";
    private static final int LIMIT_FOR_OUTPUT_FOR_SEARCH = 10;
    private static final String SEARCH_ARTICLE_BY_TITLE_OR_CATEGORY =
            "SELECT DISTINCT(a.id), a.title, a.date_published, a.views " +
                    "FROM articles AS a " +
                    "LEFT JOIN articles_categories AS aa ON a.id = aa.article_id " +
                    "LEFT JOIN categories AS c ON aa.category_id = c.id " +
                    "WHERE a.title LIKE ? " +
                    "OR c.category_name LIKE ? " +
                    "ORDER BY a.date_published DESC LIMIT " + LIMIT_FOR_OUTPUT_FOR_SEARCH + ";";

    public Article addArticle(Article article, List<Picture> pictures, List<Category> categories) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (
                PreparedStatement ps = connection.prepareStatement(ADD_NEW_ARTICLE, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psForPicture = connection.prepareStatement(SET_ARTICLE_ID_INTO_PICTURES_TABLE);
                PreparedStatement psForCategory = connection.prepareStatement(ADD_CATEGORIES_TO_ARTICLE)
        ) {
            connection.setAutoCommit(false);
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getFullText());
            ps.setTimestamp(3, article.getCreateDateAndTime());
            ps.setInt(4, article.getViews());
            ps.setLong(5, article.getAuthorId());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            article.setId(resultSet.getLong(1));
            for (Picture p : pictures) {
                psForPicture.setLong(2, p.getId());
                psForPicture.setLong(1, article.getId());
                psForPicture.executeUpdate();
            }
            for (Category category : categories) {
                psForCategory.setLong(1, category.getId());
                psForCategory.setLong(2, article.getId());
                psForCategory.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new TransactionException(e.getMessage());
            } catch (SQLException ex) {
                throw new SQLException(UNSUCCESSFUL_CONNECTION_ROLLBACK + ex.getMessage());
            }
        } finally {
            connection.setAutoCommit(true);
        }
        return article;
    }

    public void addViewOfByArticleId(long articleID) throws SQLException {
        this.jdbcTemplate.update(UPDATE_VIEWS_BY_ARTICLE_ID, articleID);
    }

    public Article articleById(long articleId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_ARTICLE_BY_ID, articleId);
        if (rowSet.next()) {
            return this.createArticleByRowSet(rowSet);
        }
        return null;
    }

    private Article createArticleByRowSet(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getLong("id"));
        article.setTitle(rowSet.getString("title"));
        article.setFullText(rowSet.getString("full_text"));
        article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
        article.setViews(rowSet.getInt("views") + 1);
        article.setAuthorId(rowSet.getLong("author_id"));
        article.setNumberOfLikes(rowSet.getInt("number_of_likes"));
        article.setAuthorName(rowSet.getString("user_name"));
        return article;
    }

    public List<Article> allArticlesByTitleOrCategory(String titleOrCategory) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(
                SEARCH_ARTICLE_BY_TITLE_OR_CATEGORY, "%" + titleOrCategory + "%", "%" + titleOrCategory + "%");
        List<Article> listFromArticles = new ArrayList<>();
        while (rowSet.next()) {
            listFromArticles.add(this.createArticleForRespByRowSet(rowSet));
        }
        return listFromArticles;
    }

    private Article createArticleForRespByRowSet(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getLong("id"));
        article.setTitle(rowSet.getString("title"));
        article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
        article.setViews(rowSet.getInt("views"));
        return article;
    }

    public List<Article> topFiveMostViewedArticlesForToday() throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(TOP_FIVE_MOST_VIEWED_ARTICLE);
        List<Article> listFromArticles = new ArrayList<>();
        while (rowSet.next()) {
            listFromArticles.add(this.createArticleForRespByRowSet(rowSet));
        }
        return listFromArticles;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        this.jdbcTemplate.update(DELETE_ARTICLE, id);
    }

    public Article editOfTitleAndFullText(Article article) throws SQLException {
        if (this.jdbcTemplate.update(UPDATE_ARTICLE_TITLE_AND_TEXT,
                article.getTitle(), article.getFullText(), article.getId()) > 0) {
            return article;
        }
        return null;
    }
}
