package sportal.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Article;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleRespDTO {

    private long id;
    private String title;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createDateAndTime;

   public ArticleRespDTO(Article article){
       this.setId(article.getId());
       this.setTitle(article.getTitle());
       this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
   }

    public static List<ArticleRespDTO> fromArticleToArticleRespDTO(List<Article> articles) {
       List<ArticleRespDTO> articleRespDTOList = new ArrayList<>();
       for (Article article : articles){
           articleRespDTOList.add(new ArticleRespDTO(article));
       }
        return articleRespDTOList;
    }
}
