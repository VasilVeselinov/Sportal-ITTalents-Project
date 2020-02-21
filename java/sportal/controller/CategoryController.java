package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.controller.models.category.CategoryRequestModel;
import sportal.controller.models.category.CategoryResponseModel;
import sportal.controller.models.category.CategoryWhitArticleIdModel;
import sportal.model.pojo.User;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.implementation.CategoryServiceImpl;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping(value = "/add_new")
    public CategoryResponseModel addNewCategory(@RequestBody CategoryRequestModel categoryRequestModel,
                                                HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CategoryServiceDTO serviceDTO = new CategoryServiceDTO(categoryRequestModel.getCategoryName());
        return new CategoryResponseModel(this.categoryService.addNewCategory(serviceDTO, user));
    }

    @PutMapping(value = "/edit")
    public CategoryResponseModel editCategories(@RequestBody CategoryRequestModel categoryRequestModel,
                                                HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CategoryServiceDTO serviceDTO =
                new CategoryServiceDTO(categoryRequestModel.getId(), categoryRequestModel.getCategoryName());
        return new CategoryResponseModel(this.categoryService.edit(serviceDTO, user));
    }

    @GetMapping(value = "/all")
    public List<CategoryResponseModel> allCategory() {
        return CategoryResponseModel.fromDTOToModel(this.categoryService.allCategories());
    }

    @DeleteMapping(value = "/delete/{" + CATEGORY_ID + "}")
    public CategoryResponseModel deleteCategory(@PathVariable(name = CATEGORY_ID) long categoryId,
                                                HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new CategoryResponseModel(this.categoryService.delete(categoryId, user));
    }

    @PutMapping(value = "/add_into_article/{" + CATEGORY_ID + "}/{" + ARTICLE_ID + "}")
    public CategoryWhitArticleIdModel addArticleIdAndCategoryId(
            @PathVariable(name = CATEGORY_ID) long categoryId,
            @PathVariable(name = ARTICLE_ID) long articleId,
            HttpSession session) throws BadRequestException, SQLException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new CategoryWhitArticleIdModel(this.categoryService.addCategoryByArticleId(categoryId, articleId, user));
    }

    @DeleteMapping(value = "/delete_category_from_article/{" + CATEGORY_ID + "}/{" + ARTICLE_ID + "}")
    public long removeCategoryFromArticle(
            @PathVariable(name = CATEGORY_ID) long categoryId, @PathVariable(name = ARTICLE_ID) long articleId,
            HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.categoryService.removeCategoryFromArticle(categoryId, articleId, user);
    }
}
