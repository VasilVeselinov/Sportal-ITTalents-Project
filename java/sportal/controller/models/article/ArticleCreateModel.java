package sportal.controller.models.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.models.category.CategoryRequestModel;
import sportal.controller.models.picture.PictureModel;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleCreateModel {

    private String title;
    private String fullText;
    private List<CategoryRequestModel> categories;
    private List<PictureModel> pictures;
}

