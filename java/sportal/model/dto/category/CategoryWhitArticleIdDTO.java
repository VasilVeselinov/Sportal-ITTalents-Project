package sportal.model.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Category;

@NoArgsConstructor
@Getter
@Setter
public class CategoryWhitArticleIdDTO {

    private long categoryId;
    private String categoryName;
    private long articleId;

    public CategoryWhitArticleIdDTO(Category category, long articleId) {
        this.setCategoryId(category.getId());
        this.setCategoryName(category.getCategoryName());
        this.setArticleId(articleId);
    }
}
