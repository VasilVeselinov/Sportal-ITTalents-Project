package sportal.model.db.dao;

import sportal.model.db.pojo.Category;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDAO {

    List<Category> allCategoriesByArticlesId(long articleID) throws SQLException;

    boolean existsCombination(long articleId, long categoryId) throws SQLException;

    void addCategoryToArticleById(long articleId, long categoryId) throws SQLException;

    void deleteCategoryFromArticleById(long articleId, long categoryId);
}
