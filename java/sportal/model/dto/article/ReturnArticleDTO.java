package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Article;


import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ReturnArticleDTO {

    private long id;
    private String title;
    private LocalDateTime createDateAndTime;

   public ReturnArticleDTO(Article article){
       this.setId(article.getId());
       this.setTitle(article.getTitle());
       this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
   }

}
