package sportal.model.service.dto;

import lombok.Getter;
import lombok.Setter;
import sportal.controller.models.category.CategoryRequestModel;
import sportal.model.db.pojo.Category;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryServiceDTO {

    private long id;
    private String categoryName;
    private long articleId;

    private CategoryServiceDTO(long id) {
        this.id = id;
    }

    public CategoryServiceDTO(long id, String categoryName) {
        this(id);
        this.categoryName = categoryName;
    }

    public static List<CategoryServiceDTO> fromModelToDTO(List<CategoryRequestModel> categories) {
        List<CategoryServiceDTO> dtoList = new ArrayList<>();
        for (CategoryRequestModel category : categories) {
            dtoList.add(new CategoryServiceDTO(category.getId()));
        }
        return dtoList;
    }

    public static List<CategoryServiceDTO> fromPOJOToDTO(List<Category> categories) {
        List<CategoryServiceDTO> dtoList = new ArrayList<>();
        for (Category category : categories) {
            dtoList.add(new CategoryServiceDTO(category.getId(), category.getCategoryName()));
        }
        return dtoList;
    }
}
