package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.category.CategoryResponseDTO;
import sportal.model.dto.picture.PictureDTO;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleFullDataDTO {

    private ArticleWithViewsAndFullTextDTO  article;
    private List<CategoryResponseDTO> categories;
    private List<PictureDTO> pictures;
    private int numberOfLikes;
    private String authorName;
}
