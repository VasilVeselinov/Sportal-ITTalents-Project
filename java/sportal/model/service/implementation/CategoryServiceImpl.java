package sportal.model.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.NotExistsObjectException;
import sportal.exception.InvalidInputException;
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

import static sportal.util.GlobalConstants.*;

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
    private static final Logger LOGGER = LogManager.getLogger(ICategoryService.class);

    @Override
    public void addNewCategory(String categoryName) {
        if (this.categoryRepository.existsByCategoryName(categoryName)) {
            throw new InvalidInputException(EXISTS_CATEGORY);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        Category category = new Category(categoryName);
        this.categoryRepository.save(category);
        LOGGER.info(SUCCESSFUL_SAVE_IN_DB);
    }

    @Override
    public void edit(CategoryServiceDTO serviceDTO) {
        Category category = new Category(serviceDTO.getId(), serviceDTO.getCategoryName());
        if (this.categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new InvalidInputException(EXISTS_CATEGORY);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.categoryRepository.save(category);
        LOGGER.info(SUCCESSFUL_UPDATE_OF_DB);
    }

    @Override
    public List<CategoryServiceDTO> allCategories() {
        List<Category> categories = this.categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        return CategoryServiceDTO.fromPOJOToDTO(categories);
    }

    @Override
    public void delete(long categoryId) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new NotExistsObjectException(NOT_EXISTS_CATEGORY);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.categoryRepository.deleteById(categoryId);
        LOGGER.info(SUCCESSFUL_DELETE_FROM_DB);
    }

    @Override
    public void addCategoryToArticle(
            long categoryId, long articleId, long userId) throws BadRequestException, SQLException {
        ArticleServiceDTO serviceDTO = this.articleService.findById(articleId);
        if (serviceDTO.getAuthorId() != userId) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new NotExistsObjectException(NOT_EXISTS_CATEGORY);
        }
        if (this.categoryDAO.existsCombination(articleId, categoryId)) {
            throw new InvalidInputException(ALREADY_COMBINATION);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.categoryDAO.add(articleId, category.get().getId());
        LOGGER.info(SUCCESSFUL_UPDATE_OF_DB);
    }

    @Override
    public void removeCategoryFromArticle(long categoryId, long articleId) throws BadRequestException, SQLException {
        this.articleService.findById(articleId);
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.categoryDAO.delete(articleId, categoryId);
        LOGGER.info(SUCCESSFUL_DELETE_FROM_DB);
    }

    @Override
    public List<CategoryServiceDTO> validateCategories(List<CategoryServiceDTO> categories) {
        return CategoryValidator.conformityCheck(this.categoryRepository.findAll(), categories);
    }

    @Override
    public List<CategoryServiceDTO> findAllByArticleId(long articleId) throws SQLException {
        return new ArrayList<>(CategoryServiceDTO.fromPOJOToDTO(this.categoryDAO.findAllByArticleId(articleId)));
    }
}
