package sportal.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Article;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ArticleAfterEditDTO {

    private long id;
    private String newTitle;
    private String newFullText;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createDateAndTime;


    public ArticleAfterEditDTO(Article article) {
        this.setId(article.getId());
        this.setNewTitle(article.getTitle());
        this.setNewFullText(article.getFullText());
        this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
    }
}
