package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.pojo.User;
import sportal.model.service.dto.CategoryServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryService {
    CategoryServiceDTO addNewCategory(CategoryServiceDTO serviceDTO,
                                      User user) throws BadRequestException;

    CategoryServiceDTO edit(CategoryServiceDTO serviceDTO, User user) throws BadRequestException;

    List<CategoryServiceDTO> allCategories();

    CategoryServiceDTO delete(long categoryId, User user) throws BadRequestException;

    CategoryServiceDTO addCategoryByArticleId(long categoryId, long articleId,
                                              User user) throws BadRequestException, SQLException;

    long removeCategoryFromArticle(long categoryId, long articleId,
                                   User user) throws BadRequestException;

    List<CategoryServiceDTO> findAllExistsCategoriseAndCheckIsValid(List<CategoryServiceDTO> categories);

    List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException;
}
