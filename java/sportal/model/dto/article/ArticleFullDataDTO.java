package sportal.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.category.CategoryResponseDTO;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.pojo.Article;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleFullDataDTO {

    private long id;
    private String title;
    private String fullText;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createDateAndTime;
    private int views;
    private List<CategoryResponseDTO> categories;
    private List<PictureDTO> pictures;
    private int numberOfLikes;
    private String authorName;

    public ArticleFullDataDTO(Article article) {
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setFullText(article.getFullText());
        this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
        this.setViews(article.getViews());
        this.setAuthorName(article.getAuthorName());
        this.setNumberOfLikes(article.getNumberOfLikes());
    }
}
