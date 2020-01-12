package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.article.ArticleCreateDTO;
import sportal.model.dto.article.ArticleEditDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Article {

    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private long authorId;
    private String authorName;

    public  Article (ArticleEditDTO articleEditDTO){
        this.setId(articleEditDTO.getArticleBeforeEditDTO().getId());
        this.setTitle(articleEditDTO.getNewTitle());
        this.setFullText(articleEditDTO.getNewFullText());
        this.setCreateDateAndTime(Timestamp.valueOf(LocalDateTime.now()));
    }

    public Article(ArticleCreateDTO validArticle) {
        this.setTitle(validArticle.getTitle());
        this.setFullText(validArticle.getFullText());
        this.setCreateDateAndTime(Timestamp.valueOf(LocalDateTime.now()));
        this.setViews(0);
    }
}
