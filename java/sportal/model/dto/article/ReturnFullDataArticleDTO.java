package sportal.model.dto.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;
import sportal.model.pojo.Comment;
import sportal.model.pojo.Picture;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class ReturnFullDataArticleDTO {

    private Article article;
    private Collection<Category> categories;
    private Collection<Picture> pictures;
    private Collection<Comment> comments;
    private int numberOfLikes;
    private String authorName;
}
