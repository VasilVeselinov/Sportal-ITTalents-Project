package sportal.controller.models.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.CategoryServiceDTO;

@NoArgsConstructor
@Getter
@Setter
public class CategoryWhitArticleIdModel {

    private long categoryId;
    private String categoryName;
    private long articleId;

    public CategoryWhitArticleIdModel(CategoryServiceDTO serviceDTO) {
        this.setCategoryId(serviceDTO.getId());
        this.setCategoryName(serviceDTO.getCategoryName());
        this.setArticleId(serviceDTO.getArticleId());
    }
}
