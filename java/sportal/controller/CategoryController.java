package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.model.dto.category.CategoryRequestDTO;
import sportal.model.dto.category.CategoryResponseDTO;
import sportal.model.dto.category.CategoryWhitArticleIdDTO;
import sportal.model.service.CategoryService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "/add_new")
    public CategoryResponseDTO addNewCategory(@RequestBody CategoryRequestDTO categoryRequestDTO,
                                              HttpSession session) throws BadRequestException {
        return this.categoryService.addNewCategory(categoryRequestDTO, session);
    }

    @PutMapping(value = "/edit")
    public CategoryResponseDTO editCategories(@RequestBody CategoryRequestDTO categoryRequestDTO,
                                              HttpSession session) throws BadRequestException {
        return this.categoryService.edit(categoryRequestDTO, session);
    }

    @GetMapping(value = "/all")
    public List<CategoryResponseDTO> allCategory() {
        return this.categoryService.allCategories();
    }

    @DeleteMapping(value = "/delete/{" + CATEGORY_ID + "}")
    public CategoryResponseDTO deleteCategory(@PathVariable(name = CATEGORY_ID) long categoryId,
                                              HttpSession session) throws BadRequestException {
        return this.categoryService.delete(categoryId, session);
    }

    @PutMapping(value = "/add_into_article/{" + CATEGORY_ID + "}/{" + ARTICLE_ID + "}")
    public CategoryWhitArticleIdDTO addArticleIdAndCategoryId(
            @PathVariable(name = CATEGORY_ID) long categoryId, @PathVariable(name = ARTICLE_ID) long articleId,
            HttpSession session) throws BadRequestException, SQLException {
        return this.categoryService.addCategoryByArticleId(categoryId,articleId, session);
    }
}
