package sportal.controller.models.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArticleEditModel {

    private long oldArticleId;
    private String newTitle;
    private String newFullText;
}
