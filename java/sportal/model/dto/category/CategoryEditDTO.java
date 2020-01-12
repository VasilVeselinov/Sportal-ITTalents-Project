package sportal.model.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Category;

@NoArgsConstructor
@Getter
@Setter
public class CategoryEditDTO {

    private Category oldCategory;
    private String newCategoryName;
}
