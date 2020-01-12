package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.category.CategoryEditDTO;
import sportal.model.dto.category.CategoryNewDTO;

@NoArgsConstructor
@Getter
@Setter
public class Category {

    private long id;
    private String categoryName;

    public Category(CategoryNewDTO categoryNewDTO){
        this.setCategoryName(categoryNewDTO.getCategoryName());
    }

    public Category(CategoryEditDTO editDTO){
        this.setId(editDTO.getOldCategory().getId());
        this.setCategoryName(editDTO.getNewCategoryName());
    }
}
