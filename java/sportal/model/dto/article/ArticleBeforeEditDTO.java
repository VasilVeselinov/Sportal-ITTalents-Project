package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArticleBeforeEditDTO {

    private long id;
    private String oldTitle;
    private String oldFullText;
}
