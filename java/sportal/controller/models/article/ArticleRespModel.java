package sportal.controller.models.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.ArticleServiceDTO;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleRespModel {

    private long id;
    private String title;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createDateAndTime;
    private int views;

   public ArticleRespModel(ArticleServiceDTO serviceDTO){
       this.setId(serviceDTO.getId());
       this.setTitle(serviceDTO.getTitle());
       this.setCreateDateAndTime(serviceDTO.getCreateDateAndTime().toLocalDateTime());
       this.setViews(serviceDTO.getViews());
   }

    public static List<ArticleRespModel> fromDTOToModel(List<ArticleServiceDTO> articles) {
       List<ArticleRespModel> articleRespModelList = new ArrayList<>();
       for (ArticleServiceDTO article : articles){
           articleRespModelList.add(new ArticleRespModel(article));
       }
        return articleRespModelList;
    }
}
