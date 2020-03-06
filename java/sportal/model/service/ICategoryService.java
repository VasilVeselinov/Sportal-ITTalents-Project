package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.CategoryServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryService {

    String YOU_ARE_NOT_AUTHOR = "You are not author of this article!";
    String ALREADY_COMBINATION = "Exists this combination!";

    void addNewCategory(String categoryName);

    void edit(CategoryServiceDTO serviceDTO);

    List<CategoryServiceDTO> allCategories();

    void delete(long categoryId);

    void addCategoryToArticle(long categoryId, long articleId, long userId) throws BadRequestException, SQLException;

    void removeCategoryFromArticle(long categoryId, long articleId) throws BadRequestException, SQLException;

    List<CategoryServiceDTO> findAllExistsCategoriseAndCheckIsValid(List<CategoryServiceDTO> categories);

    List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException;
}
