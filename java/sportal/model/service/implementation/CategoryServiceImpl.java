package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.CategoryDAO;
import sportal.model.service.IArticleService;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.validators.CategoryValidator;
import sportal.model.validators.UserValidator;
import sportal.model.db.pojo.Category;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.CategoryRepository;
import sportal.model.service.ICategoryService;
import sportal.model.service.dto.CategoryServiceDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sportal.model.validators.AbstractValidator.*;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private static final String EXISTS_CATEGORY = "That category exists!";
    private static final String THIS_CATEGORY_NOT_EXISTS = "This category not exists!";

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private IArticleService articleService;

    @Override
    public void addNewCategory(CategoryServiceDTO serviceDTO, User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        CategoryServiceDTO validCategoryDTO = CategoryValidator.checkForValidNewCategory(serviceDTO);
        if (this.categoryRepository.existsByCategoryName(validCategoryDTO.getCategoryName())) {
            throw new ExistsObjectException(EXISTS_CATEGORY);
        }
        Category category = new Category(validCategoryDTO.getCategoryName());
        this.categoryRepository.save(category);
    }

    @Override
    public void edit(CategoryServiceDTO serviceDTO, User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        CategoryServiceDTO validCategoryDTO = CategoryValidator.checkForValidData(serviceDTO);
        Category category = new Category(validCategoryDTO);
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
    public void delete(long categoryId, User user) throws BadRequestException {
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
    }

    @Override
    public void addCategoryToArticle(long categoryId, long articleId,
                                     User user) throws BadRequestException, SQLException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        ArticleServiceDTO serviceDTO = this.articleService.findArticleById(articleId);
        if (serviceDTO.getAuthorId() != user.getId()) {
            throw new BadRequestException(YOU_ARE_NOT_AUTHOR);
        }
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ExistsObjectException(THIS_CATEGORY_NOT_EXISTS);
        }
        if (this.categoryDAO.existsCombination(articleId, categoryId)) {
            throw new ExistsObjectException(ALREADY_COMBINATION);
        }
        this.categoryDAO.addCategoryToArticleById(articleId, category.get().getId());
    }

    @Override
    public void removeCategoryFromArticle(long categoryId, long articleId,
                                          User user) throws BadRequestException, SQLException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        ArticleServiceDTO serviceDTO = this.articleService.findArticleById(articleId);
        if (serviceDTO.getAuthorId() != user.getId()) {
            throw new BadRequestException(YOU_ARE_NOT_AUTHOR);
        }
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
