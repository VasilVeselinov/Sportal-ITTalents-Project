package sportal.model.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryEditDTO {

    private long oldCategoryId;
    private String newCategoryName;
}
