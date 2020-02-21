package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.CategoryServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryService {
    void addNewCategory(CategoryServiceDTO serviceDTO, User user) throws BadRequestException;

    void edit(CategoryServiceDTO serviceDTO, User user) throws BadRequestException;

    List<CategoryServiceDTO> allCategories();

    void delete(long categoryId, User user) throws BadRequestException;

    void addCategoryToArticle(long categoryId, long articleId, User user) throws BadRequestException, SQLException;

    void removeCategoryFromArticle(long categoryId, long articleId, User user) throws BadRequestException, SQLException;

    List<CategoryServiceDTO> findAllExistsCategoriseAndCheckIsValid(List<CategoryServiceDTO> categories);

    List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException;
}
