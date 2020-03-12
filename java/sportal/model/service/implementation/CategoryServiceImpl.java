package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.ICategoryDAO;
import sportal.model.service.IArticleService;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.validators.CategoryValidator;
import sportal.model.db.pojo.Category;
import sportal.model.db.repository.CategoryRepository;
import sportal.model.service.ICategoryService;
import sportal.model.service.dto.CategoryServiceDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private static final String EXISTS_CATEGORY = "That category exists!";
    private static final String NOT_EXISTS_CATEGORY = "This category not exists!";
    private static final String YOU_ARE_NOT_AUTHOR = "You are not author of this article!";
    private static final String ALREADY_COMBINATION = "Exists this combination!";

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ICategoryDAO categoryDAO;
    @Autowired
    private IArticleService articleService;

    @Override
    public void addNewCategory(String categoryName) {
        if (this.categoryRepository.existsByCategoryName(categoryName)) {
            throw new ExistsObjectException(EXISTS_CATEGORY);
        }
        Category category = new Category(categoryName);
        this.categoryRepository.save(category);
    }

    @Override
    public void edit(CategoryServiceDTO serviceDTO) {
        Category category = new Category(serviceDTO.getId(), serviceDTO.getCategoryName());
        if (this.categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new ExistsObjectException(EXISTS_CATEGORY);
        }
        this.categoryRepository.save(category);
    }

    @Override
    public List<CategoryServiceDTO> allCategories() {
        List<Category> categories = this.categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return CategoryServiceDTO.fromPOJOToDTO(categories);
    }

    @Override
    public void delete(long categoryId) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_CATEGORY);
        }
        this.categoryRepository.deleteById(categoryId);
    }

    @Override
    public void addCategoryToArticle(
            long categoryId, long articleId, long userId) throws BadRequestException, SQLException {
        ArticleServiceDTO serviceDTO = this.articleService.findArticleById(articleId);
        if (serviceDTO.getAuthorId() != userId) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_CATEGORY);
        }
        if (this.categoryDAO.existsCombination(articleId, categoryId)) {
            throw new ExistsObjectException(ALREADY_COMBINATION);
        }
        this.categoryDAO.addCategoryToArticleById(articleId, category.get().getId());
    }

    @Override
    public void removeCategoryFromArticle(long categoryId, long articleId) throws BadRequestException, SQLException {
        this.articleService.findArticleById(articleId);
        this.categoryDAO.deleteCategoryFromArticleById(articleId, categoryId);
    }

    @Override
    public List<CategoryServiceDTO> findAllExistsCategoriseAndCheckIsValid(List<CategoryServiceDTO> categories) {
        return CategoryValidator.conformityCheck(this.categoryRepository.findAll(), categories);
    }

    @Override
    public List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException {
        return new ArrayList<>(
                CategoryServiceDTO.fromPOJOToDTO(this.categoryDAO.allCategoriesByArticlesId(articleId)));
    }
}
