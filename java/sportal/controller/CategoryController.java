package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.CategoryDAO;
import sportal.model.data_validators.CategoryValidator;
import sportal.model.data_validators.SessionValidator;
import sportal.model.dto.category.CategoryEditDTO;
import sportal.model.dto.category.CategoryResponseDTO;
import sportal.model.pojo.Category;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryDAO categoriesDAO;

    @PutMapping(value = "/categories")
    public CategoryResponseDTO editCategories(@RequestBody CategoryEditDTO categoryEditDTO,
                                              HttpSession session) throws SQLException, BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        CategoryEditDTO validCategoryEditDTO = CategoryValidator.checkForValidData(categoryEditDTO);
        Category category = new Category(validCategoryEditDTO);
        Category editedCategory = this.categoriesDAO.editCategory(category);
        if (editedCategory != null) {
            return new CategoryResponseDTO(category);
        } else {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
    }

    @GetMapping(value = "/categories")
    public List<CategoryResponseDTO> allCategory() throws SQLException {
        List<Category> listWhitCategories = this.categoriesDAO.allCategories();
        return CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(listWhitCategories);
    }

    @DeleteMapping(value = "/categories/{" + CATEGORY_ID + "}")
    public CategoryResponseDTO deleteCategory(@PathVariable(name = CATEGORY_ID) long categoryId,
                               HttpSession session) throws SQLException, BadRequestException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Category category = this.categoriesDAO.findById(categoryId);
        if (category == null) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        this.categoriesDAO.deleteById(categoryId);
        return new  CategoryResponseDTO (category);
    }
}
