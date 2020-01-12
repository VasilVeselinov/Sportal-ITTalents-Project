package sportal.model.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Category;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CategoryResponseDTO {

    private long id;
    private String categoryName;

    public CategoryResponseDTO(Category category) {
        this.setId(category.getId());
        this.setCategoryName(category.getCategoryName());
    }

    public static List<CategoryResponseDTO> fromCategoryListToCategoryResponseDTO(List<Category> categories) {
        List<CategoryResponseDTO> responseDTOList = new ArrayList<>();
        for (Category category : categories) {
            responseDTOList.add(new CategoryResponseDTO(category));
        }
        return responseDTOList;
    }
}
