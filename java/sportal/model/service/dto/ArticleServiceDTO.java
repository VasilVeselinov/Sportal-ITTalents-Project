package sportal.model.service.dto;

import lombok.Getter;
import lombok.Setter;
import sportal.model.db.pojo.Article;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArticleServiceDTO {

    private long id;
    private String title;
    private String fullText;
    private Timestamp createDateAndTime;
    private int views;
    private int numberOfLikes;
    private long authorId;
    private List<CategoryServiceDTO> categories;
    private List<PictureServiceDTO> pictures;
    private String authorName;

    public ArticleServiceDTO(String title, String fullText,
                             List<CategoryServiceDTO> categories, List<PictureServiceDTO> pictures) {
        this.title = title;
        this.fullText = fullText;
        this.categories = categories;
        this.pictures = pictures;
    }

    public ArticleServiceDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.createDateAndTime = article.getCreateDateAndTime();
        this.views = article.getViews();
    }

    public ArticleServiceDTO(Article article, List<PictureServiceDTO> pictures, List<CategoryServiceDTO> categories) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.fullText = article.getFullText();
        this.createDateAndTime = article.getCreateDateAndTime();
        this.views = article.getViews();
        this.categories = categories;
        this.pictures = pictures;
        this.numberOfLikes = article.getNumberOfLikes();
    }

    public ArticleServiceDTO(long oldArticleId, String newTitle, String newFullText) {
        this.id = oldArticleId;
        this.title = newTitle;
        this.fullText = newFullText;
    }

    public static List<ArticleServiceDTO> fromPOJOToDTO(List<Article> articles) {
        List<ArticleServiceDTO> articleRespModelList = new ArrayList<>();
        for (Article article : articles) {
            articleRespModelList.add(new ArticleServiceDTO(article));
        }
        return articleRespModelList;
    }
}
