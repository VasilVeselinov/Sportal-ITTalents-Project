package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.controller.models.category.CategoryRequestModel;
import sportal.controller.models.category.CategoryResponseModel;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.implementation.CategoryServiceImpl;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping(value = "/add_new")
    public ResponseEntity<Void> addNewCategory(@RequestBody CategoryRequestModel categoryModel,
                                               HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CategoryServiceDTO serviceDTO = new CategoryServiceDTO(categoryModel.getId(), categoryModel.getCategoryName());
        this.categoryService.addNewCategory(serviceDTO, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/categories/all");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PutMapping(value = "/edit")
    public void editCategories(@RequestBody CategoryRequestModel categoryModel,
                               HttpSession session,
                               HttpServletResponse response) throws BadRequestException, IOException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CategoryServiceDTO serviceDTO = new CategoryServiceDTO(categoryModel.getId(), categoryModel.getCategoryName());
        this.categoryService.edit(serviceDTO, user);
        response.sendRedirect("/categories/all");
    }

    @GetMapping(value = "/all")
    public List<CategoryResponseModel> allCategory() {
        return CategoryResponseModel.fromDTOToModel(this.categoryService.allCategories());
    }

    @DeleteMapping(value = "/delete/{" + CATEGORY_ID + "}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = CATEGORY_ID) long categoryId,
                                               HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.categoryService.delete(categoryId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/categories/all");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PutMapping(value = "/add_into_article/{" + CATEGORY_ID + "}/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> addCategoryToArticle(@PathVariable(name = CATEGORY_ID) long categoryId,
                                                     @PathVariable(name = ARTICLE_ID) long articleId,
                                                     HttpSession session) throws BadRequestException, SQLException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.categoryService.addCategoryToArticle(categoryId, articleId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/delete_category_from_article/{" + CATEGORY_ID + "}/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> removeCategoryFromArticle(
            @PathVariable(name = CATEGORY_ID) long categoryId,
            @PathVariable(name = ARTICLE_ID) long articleId,
            HttpSession session) throws BadRequestException, SQLException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.categoryService.removeCategoryFromArticle(categoryId, articleId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
