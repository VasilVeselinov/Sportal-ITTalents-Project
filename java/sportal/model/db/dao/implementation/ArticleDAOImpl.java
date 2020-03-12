package sportal.model.db.dao.implementation;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import sportal.annotations.DAOAnnotation;
import sportal.exception.TransactionException;
import sportal.model.db.dao.DAO;
import sportal.model.db.dao.IArticleDAO;
import sportal.model.db.pojo.Article;
import sportal.model.db.pojo.Category;
import sportal.model.db.pojo.Picture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@DAOAnnotation
public class ArticleDAOImpl extends DAO implements IArticleDAO {

    private static final String ADD_NEW_ARTICLE =
            "INSERT INTO articles (title, full_text, date_published, views, author_id) " +
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
    private static final String UPDATE_VIEWS_BY_ARTICLE_ID = "UPDATE articles SET views = views + 1 WHERE id = ?;";
    private static final String FIND_ARTICLE_BY_ID =
            "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id, " +
                    "u.username, COUNT(ula.user_id) AS number_of_likes " +
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
    private static final int LIMIT_FOR_OUTPUT_FOR_SEARCH_BY_CATEGORY_ID = 5;
    private static final String ARTICLES_BY_CATEGORY_ID =
            "SELECT a.id, a.title, a.date_published, a.views " +
                    "FROM articles AS a " +
                    "JOIN articles_categories AS ac ON a.id = ac.article_id " +
                    "JOIN categories AS c ON c.id = ac.category_id " +
                    "WHERE category_id = ? " +
                    "ORDER BY a.date_published DESC LIMIT " + LIMIT_FOR_OUTPUT_FOR_SEARCH_BY_CATEGORY_ID + ";";
    private static final String FIND_COMBINATION =
            "SELECT article_id, user_id " +
                    "FROM users_like_articles " +
                    "WHERE article_id = ? AND user_id = ?";

    @Override
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

    @Override
    public void addViewOfByArticleId(long articleID) throws SQLException {
        this.jdbcTemplate.update(UPDATE_VIEWS_BY_ARTICLE_ID, articleID);
    }

    @Override
    public Article findById(long articleId) throws SQLException {
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
        article.setAuthorName(rowSet.getString("username"));
        return article;
    }

    @Override
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

    @Override
    public List<Article> topFiveMostViewedArticlesForToday() throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(TOP_FIVE_MOST_VIEWED_ARTICLE);
        List<Article> listFromArticles = new ArrayList<>();
        while (rowSet.next()) {
            listFromArticles.add(this.createArticleForRespByRowSet(rowSet));
        }
        return listFromArticles;
    }

    @Override
    public List<Article> articlesByCategoryId(long categoryID) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ARTICLES_BY_CATEGORY_ID, categoryID);
        List<Article> listWithCategories = new ArrayList<>();
        while (rowSet.next()) {
            listWithCategories.add(this.createByRowSet(rowSet));
        }

        return listWithCategories;
    }

    private Article createByRowSet(SqlRowSet rowSet) {
        Article article = new Article();
        article.setId(rowSet.getInt("id"));
        article.setTitle(rowSet.getString("title"));
        article.setCreateDateAndTime(rowSet.getTimestamp("date_published"));
        article.setViews(rowSet.getInt("views"));
        return article;
    }

    @Override
    public boolean existsVoteForThatArticleFromThisUser(long articleId, long userId) {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_COMBINATION, articleId, userId);
        return rowSet.next();
    }
}
