package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.CategoryServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_ADMIN;
import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface ICategoryService {

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    void addNewCategory(String categoryName);

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    void edit(CategoryServiceDTO serviceDTO);

    List<CategoryServiceDTO> allCategories();

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    void delete(long categoryId);

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    void addCategoryToArticle(long categoryId, long articleId, long userId) throws BadRequestException, SQLException;

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    void removeCategoryFromArticle(long categoryId, long articleId) throws BadRequestException, SQLException;

    List<CategoryServiceDTO> findAllExistsCategoriseAndCheckIsValid(List<CategoryServiceDTO> categories);

    List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException;
}
