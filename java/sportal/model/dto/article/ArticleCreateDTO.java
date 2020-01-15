package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.category.CategoryRequestDTO;
import sportal.model.dto.picture.PictureDTO;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleCreateDTO {

    private String title;
    private String fullText;
    private List<CategoryRequestDTO> categories;
    private List<PictureDTO> pictures;
}

