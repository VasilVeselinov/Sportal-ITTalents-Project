package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Article;

@NoArgsConstructor
@Getter
@Setter
public class ArticleAfterEditDTO {

    private long id;
    private String newTitle;
    private String newFullText;


    public ArticleAfterEditDTO(Article article) {
        this.setId(article.getId());
        this.setNewTitle(article.getTitle());
        this.setNewFullText(article.getFullText());
    }
}
