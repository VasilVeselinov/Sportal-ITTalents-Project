package sportal.model.db.dao;

import sportal.model.db.pojo.Article;
import sportal.model.db.pojo.Category;
import sportal.model.db.pojo.Picture;

import java.sql.SQLException;
import java.util.List;

public interface IArticleDAO {

    Article addArticle(Article article, List<Picture> pictures, List<Category> categories) throws SQLException;

    void addViewOfByArticleId(long articleID) throws SQLException;

    Article findById(long articleId) throws SQLException;

    List<Article> allArticlesByTitleOrCategory(String titleOrCategory, int limit, int offset) throws SQLException;

    List<Article> topFiveMostViewedArticlesForToday() throws SQLException;

    List<Article> articlesByCategoryId(long categoryID, int limit, int offset) throws SQLException;

    boolean isArticleLikedByUser(long articleId, long userId);
}
