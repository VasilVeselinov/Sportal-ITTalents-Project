package sportal.controller.models.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.validation.TextValidation;
import sportal.controller.validation.TittleValidation;

import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@Setter
public class ArticleEditModel {


    @Positive(message = "Id must be greater than 0!")
    private long oldArticleId;
    @TittleValidation
    private String newTitle;
    @TextValidation
    private String newFullText;
}
