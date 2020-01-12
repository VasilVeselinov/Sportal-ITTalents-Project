package sportal.model.DAO;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.exception.TransactionException;
import sportal.model.DAO.interfaceDAO.IDAODeleteById;
import sportal.model.pojo.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleDAO extends DAO implements IDAODeleteById {

    private static final String INSERT_ARTICLE =
            "INSERT INTO articles (title ,full_text, date_published, views, author_id) VALUES (?, ?, ?, ?, ?);";
    private static final String TOP_FIVE_MOST_VIEWED_ARTICLE =
            "SELECT id, title, date_published " +
                    "FROM articles " +
                    "WHERE DATE(date_published) = CURRENT_DATE() " +
                    "ORDER BY views DESC LIMIT 5;";
    private static final String UPDATE_ARTICLE = "UPDATE articles SET title = ?, full_text = ? WHERE id = ?;";
    private static final String UPDATE_VIEWS = "UPDATE articles SET views = views + 1 WHERE id = ?;";
    private static final String DELETE_SQL = "DELETE FROM articles WHERE id = ?;";
    private static final String DELETE_ALL_CATEGORIES_BY_ARTICLE_ID = "DELETE FROM articles_categories WHERE article_id = ?;";
    private static final String DELETE_ALL_PICTURES_BY_ARTICLE_ID = "DELETE FROM pictures WHERE article_id = ?;";
    private static final String SEARCH_ARTICLE_BY_ID =
            "SELECT a.id, a.title, a.full_text, a.date_published, a.views, a.author_id, u.user_name " +
                    "FROM articles AS a " +
                    "LEFT JOIN users AS u ON a.author_id = u.id " +
                    "WHERE a.id = ?;";
    private static final int NUMBER_OF_LIMIT_FOR_SEARCH_BY_TITLE_OR_CATEGORY = 5;
    public Article addArticle(Article article) throws SQLException {
        try (
                Connection connection = this.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getFullText());
            ps.setTimestamp(3, article.getCreateDateAndTime());
            ps.setInt(4, article.getViews());
            ps.setLong(5, article.getAuthorId());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            article.setId(resultSet.getLong(1));
            return article;
        }
    }

    public void addViewOfByArticleId(long articleID) throws SQLException {
        this.jdbcTemplate.update(UPDATE_VIEWS, articleID);
    }

    public Article articleById(long articleId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(SEARCH_ARTICLE_BY_ID, articleId);
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
        article.setAuthorName(rowSet.getString("user_name"));
        return article;
    }

    public List<Article> allArticlesByTitleOrCategory(String titleOrCategory) throws SQLException {
        String FIND_ARTICLE_BY_TITLE_OR_CATEGORY =
                "SELECT a.id, a.title, a.date_published " +
                        "FROM articles AS a " +
                        "LEFT JOIN articles_categories AS aa ON a.id = aa.article_id " +
                        "LEFT JOIN categories AS c ON aa.category_id = c.id " +
                        "WHERE a.title LIKE '" + titleOrCategory + "%' " +
                        "OR c.category_name LIKE '" + titleOrCategory + "%' LIMIT " +
                        NUMBER_OF_LIMIT_FOR_SEARCH_BY_TITLE_OR_CATEGORY +";";
        final SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_ARTICLE_BY_TITLE_OR_CATEGORY);
        final List<Article> listFromArticles = new ArrayList<>();
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
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (
                PreparedStatement psForSetFKFalse = connection.prepareStatement(SET_FK_FALSE);
                PreparedStatement psForArticle = connection.prepareStatement(DELETE_SQL);
                PreparedStatement psForCategories = connection.prepareStatement(DELETE_ALL_CATEGORIES_BY_ARTICLE_ID);
                PreparedStatement psForPictures = connection.prepareStatement(DELETE_ALL_PICTURES_BY_ARTICLE_ID);
                PreparedStatement psForSetFKTrue = connection.prepareStatement(SET_FK_TRUE)
        ) {
            connection.setAutoCommit(false);
            psForSetFKFalse.executeUpdate();
            psForArticle.setLong(1, id);
            psForArticle.executeUpdate();
            psForCategories.setLong(1, id);
            psForCategories.executeUpdate();
            psForPictures.setLong(1, id);
            psForPictures.executeUpdate();
            psForSetFKTrue.executeUpdate();
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

    public int editOfTitleAndFullText(Article article) {
        return this.jdbcTemplate.update(UPDATE_ARTICLE, article.getTitle(), article.getFullText(), article.getId());
    }
}
