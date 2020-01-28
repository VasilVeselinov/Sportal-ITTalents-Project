package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.ArticlesCategoriesDAO;
import sportal.model.data_validators.ArticleValidator;
import sportal.model.data_validators.CategoryValidator;
import sportal.model.data_validators.SessionValidator;
import sportal.model.dto.category.CategoryRequestDTO;
import sportal.model.dto.category.CategoryResponseDTO;
import sportal.model.dto.category.CategoryWhitArticleIdDTO;
import sportal.model.pojo.Category;
import sportal.model.pojo.ExistsObject;
import sportal.model.pojo.User;
import sportal.model.repository.CategoryRepository;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.WRONG_REQUEST;

@Service
public class CategoryService {

    private static final String EXISTS_CATEGORY = "That category exists!";
    private static final String THIS_CATEGORY_NOT_EXISTS = "This category not exists!";

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;

    public CategoryResponseDTO addNewCategory(CategoryRequestDTO categoryRequestDTO,
                                              HttpSession session) throws BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        CategoryRequestDTO validCategoryDTO = CategoryValidator.checkForValidNewCategory(categoryRequestDTO);
        Category category = new Category(validCategoryDTO.getCategoryName());

        Category saveCategory = this.categoryRepository.save(category);
        return new CategoryResponseDTO(saveCategory);
    }

    public CategoryResponseDTO edit(CategoryRequestDTO categoryRequestDTO, HttpSession session) throws BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        CategoryRequestDTO validCategoryRequestDTO = CategoryValidator.checkForValidData(categoryRequestDTO);
        Category category = new Category(validCategoryRequestDTO);
        if (this.categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new ExistsObjectException(EXISTS_CATEGORY);
        }
        Category saveCategory = this.categoryRepository.save(category);
        return new CategoryResponseDTO(saveCategory);
    }

    public List<CategoryResponseDTO> allCategories() {
        List<Category> categories = this.categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(categories);
    }

    public CategoryResponseDTO delete(long categoryId, HttpSession session) throws BadRequestException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ExistsObjectException(THIS_CATEGORY_NOT_EXISTS);
        }
        this.categoryRepository.deleteById(categoryId);
        return new CategoryResponseDTO(category.get());
    }

    public CategoryWhitArticleIdDTO addCategoryByArticleId(
            long categoryId, long articleId, HttpSession session) throws BadRequestException, SQLException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ExistsObjectException(THIS_CATEGORY_NOT_EXISTS);
        }
        List<ExistsObject> objectList = this.articlesCategoriesDAO.existsCombinationAndArticleId(articleId);
        ArticleValidator.validation(objectList, articleId, category.get().getId());
        this.articlesCategoriesDAO.addCategoryIdAndArticleId(articleId, category.get().getId());
        return new CategoryWhitArticleIdDTO(category.get(), articleId);
    }
}
