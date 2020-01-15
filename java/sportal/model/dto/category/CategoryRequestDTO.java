package sportal.model.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryRequestDTO {

    private long categoryId;
    private String categoryName;
}
