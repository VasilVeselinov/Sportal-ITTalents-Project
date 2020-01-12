package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;
import sportal.model.pojo.Picture;
import sportal.model.pojo.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class NewArticleDTO {

    private long id;
    private String title;
    private String fullText;
    private LocalDateTime createDateAndTime;
    private Collection<Category> categories;
    private Collection<Picture> pictures;
    private int views;
    private User author;

    public NewArticleDTO(Article article, Collection<Category> categories, Collection<Picture> pictures, User author){
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
