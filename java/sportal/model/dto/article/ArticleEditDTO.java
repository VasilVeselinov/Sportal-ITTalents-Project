package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArticleEditDTO {

    private ArticleBeforeEditDTO articleBeforeEditDTO;
    private String newTitle;
    private String newFullText;
}
