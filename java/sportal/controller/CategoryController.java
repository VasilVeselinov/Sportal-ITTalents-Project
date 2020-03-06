package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sportal.controller.util.AuthValidator;
import sportal.controller.models.user.UserLoginModel;
import sportal.controller.validation.NameValid;
import sportal.exception.BadRequestException;
import sportal.controller.models.category.CategoryRequestModel;
import sportal.controller.models.category.CategoryResponseModel;
import sportal.model.service.ICategoryService;
import sportal.model.service.dto.CategoryServiceDTO;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping(value = "/add_new")
    public ResponseEntity<Void> addNewCategory(
            @RequestParam(name = "text", required = false) @NameValid String categoryName, HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsAdmin(logUser);
        this.categoryService.addNewCategory(categoryName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/categories/all");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PutMapping(value = "/edit")
    public void editCategories(@Valid @RequestBody CategoryRequestModel categoryModel, BindingResult bindingResult,
                               HttpSession session, HttpServletResponse response) throws IOException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsAdmin(logUser);
        CategoryServiceDTO serviceDTO = new CategoryServiceDTO(categoryModel.getId(), categoryModel.getCategoryName());
        this.categoryService.edit(serviceDTO);
        response.sendRedirect("/categories/all");
    }

    @GetMapping(value = "/all")
    public List<CategoryResponseModel> allCategory() {
        return CategoryResponseModel.fromDTOToModel(this.categoryService.allCategories());
    }

    @DeleteMapping(value = "/delete/{" + CATEGORY_ID + "}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable(name = CATEGORY_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long categoryId,
            HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsEditor(logUser);
        this.categoryService.delete(categoryId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/categories/all");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PutMapping(value = "/add_into_article/{" + CATEGORY_ID + "}/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> addCategoryToArticle(
            @PathVariable(name = CATEGORY_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long categoryId,
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws BadRequestException, SQLException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsAdmin(logUser);
        this.categoryService.addCategoryToArticle(categoryId, articleId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/delete_category_from_article/{" + CATEGORY_ID + "}/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> removeCategoryFromArticle(
            @PathVariable(name = CATEGORY_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long categoryId,
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws BadRequestException, SQLException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsEditor(logUser);
        this.categoryService.removeCategoryFromArticle(categoryId, articleId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
