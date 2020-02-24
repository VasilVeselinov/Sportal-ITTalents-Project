package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryService {
    void addNewCategory(String categoryName, UserServiceDTO userOfSession) throws BadRequestException;

    void edit(CategoryServiceDTO serviceDTO, UserServiceDTO userOfSession) throws BadRequestException;

    List<CategoryServiceDTO> allCategories();

    void delete(long categoryId, UserServiceDTO userOfSession) throws BadRequestException;

    void addCategoryToArticle(long categoryId, long articleId, UserServiceDTO userOfSession) throws BadRequestException, SQLException;

    void removeCategoryFromArticle(long categoryId, long articleId, UserServiceDTO userOfSession) throws BadRequestException, SQLException;

    List<CategoryServiceDTO> findAllExistsCategoriseAndCheckIsValid(List<CategoryServiceDTO> categories);

    List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException;
}
