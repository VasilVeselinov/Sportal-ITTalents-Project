package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.category.CategoryRequestDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Category {

    private long id;
    private String categoryName;

    public Category(CategoryRequestDTO editDTO) {
        this.setId(editDTO.getCategoryId());
        this.setCategoryName(editDTO.getCategoryName());
    }

    public static List<Category> fromCategoryRequestDTOToCategory(List<CategoryRequestDTO> categories) {
        List<Category> categoryList =new ArrayList<>();
        for (CategoryRequestDTO categoryRequestDTO: categories){
         categoryList.add(new Category(categoryRequestDTO));
        }
        return categoryList;
    }
}
