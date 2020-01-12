package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Category;
import sportal.model.pojo.Picture;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class CreateArticleDTO {

    private String title;
    private String fullText;
    private Collection<Category> categories;
    private Collection<Picture> pictures;
}
