package sportal.model.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleAfterCreateDTO {

    private long id;
    private String title;
    private String fullText;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDateAndTime;
    private List<Category> categories;
    private List<PictureDTO> pictures;
    private int views;
    private UserResponseDTO author;

    public ArticleAfterCreateDTO(Article article, List<Category> categories,
                                 List<PictureDTO> pictures,
                                 UserResponseDTO author){
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setFullText(article.getFullText());
        this.setCreateDateAndTime(article.getCreateDateAndTime().toLocalDateTime());
        this.setAuthor(author);
        this.setViews(article.getViews());
        this.setCategories(categories);
        this.setPictures(pictures);
    }
}
