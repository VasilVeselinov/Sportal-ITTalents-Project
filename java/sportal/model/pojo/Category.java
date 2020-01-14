package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.category.CategoryEditDTO;

@NoArgsConstructor
@Getter
@Setter
public class Category {

    private long id;
    private String categoryName;

    public Category(CategoryEditDTO editDTO) {
        this.setId(editDTO.getOldCategoryId());
        this.setCategoryName(editDTO.getNewCategoryName());
    }
}
