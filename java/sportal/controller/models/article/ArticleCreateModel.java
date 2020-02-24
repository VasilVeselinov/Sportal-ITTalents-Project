package sportal.controller.models.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.models.category.CategoryRequestModel;
import sportal.controller.models.picture.PictureModel;
import sportal.controller.validation.TextValidation;
import sportal.controller.validation.TittleValidation;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleCreateModel {

    @TittleValidation
    private String title;
    @TextValidation
    private String fullText;
    @NotEmpty(message = "Have not a category!")
    private List<CategoryRequestModel> categories;
    @NotEmpty(message = "Have not a picture!")
    private List<PictureModel> pictures;
}

