package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.ArticlesCategoriesDAO;
import sportal.model.data_validators.ArticleValidator;
import sportal.model.data_validators.CategoryValidator;
import sportal.model.data_validators.UserValidator;
import sportal.model.pojo.Category;
import sportal.model.pojo.ExistsObject;
import sportal.model.pojo.User;
import sportal.model.repository.CategoryRepository;
import sportal.model.service.ICategoryService;
import sportal.model.service.dto.CategoryServiceDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.NOT_ALLOWED_OPERATION;
import static sportal.model.data_validators.AbstractValidator.WRONG_REQUEST;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private static final String EXISTS_CATEGORY = "That category exists!";
    private static final String THIS_CATEGORY_NOT_EXISTS = "This category not exists!";

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;

    @Override
    public CategoryServiceDTO addNewCategory(CategoryServiceDTO serviceDTO,
                                             User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        CategoryServiceDTO validCategoryDTO = CategoryValidator.checkForValidNewCategory(serviceDTO);
        Category category = new Category(validCategoryDTO.getCategoryName());
        Category saveCategory = this.categoryRepository.save(category);
        return new CategoryServiceDTO(saveCategory);
    }

    @Override
    public CategoryServiceDTO edit(CategoryServiceDTO serviceDTO, User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        CategoryServiceDTO validCategoryDTO = CategoryValidator.checkForValidData(serviceDTO);
        Category category = new Category(validCategoryDTO);
        if (this.categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new ExistsObjectException(EXISTS_CATEGORY);
        }
        Category saveCategory = this.categoryRepository.save(category);
        return new CategoryServiceDTO(saveCategory);
    }

    @Override
    public List<CategoryServiceDTO> allCategories() {
        List<Category> categories = this.categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return CategoryServiceDTO.fromPOJOToDTO(categories);
    }

    @Override
    public CategoryServiceDTO delete(long categoryId, User user) throws BadRequestException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ExistsObjectException(THIS_CATEGORY_NOT_EXISTS);
        }
        this.categoryRepository.deleteById(categoryId);
        return new CategoryServiceDTO(category.get());
    }

    @Override
    public CategoryServiceDTO addCategoryByArticleId(
            long categoryId, long articleId, User user) throws BadRequestException, SQLException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ExistsObjectException(THIS_CATEGORY_NOT_EXISTS);
        }
        List<ExistsObject> objectList = this.articlesCategoriesDAO.existsCombinationAndArticleId(articleId);
        ArticleValidator.validation(objectList, articleId, category.get().getId());
        this.articlesCategoriesDAO.addCategoryIdAndArticleId(articleId, category.get().getId());
        return new CategoryServiceDTO(category.get(), articleId);
    }

    @Override
    public long removeCategoryFromArticle(long categoryId, long articleId,
                                          User user) throws BadRequestException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        if (this.articlesCategoriesDAO.delete(categoryId, articleId) > 0) {
            return categoryId;
        } else {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    @Override
    public List<CategoryServiceDTO> findAllExistsCategoriseAndCheckIsValid(List<CategoryServiceDTO> categories) {
        return CategoryValidator.conformityCheck(this.categoryRepository.findAll(), categories);
    }

    @Override
    public List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException {
        return new ArrayList<>(
                CategoryServiceDTO.fromPOJOToDTO(this.articlesCategoriesDAO.allCategoriesByArticlesId(articleId)));
    }
}
