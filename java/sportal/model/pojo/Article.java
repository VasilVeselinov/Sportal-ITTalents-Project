package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.models.article.ArticleCreateModel;
import sportal.controller.models.article.ArticleEditModel;
import sportal.model.service.dto.ArticleServiceDTO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String fullText;
    @Column(name = "date_published")
    private Timestamp createDateAndTime;
    private int views;
    @Transient
    private int numberOfLikes;
    private long authorId;
    @Transient
    private String authorName;

    public Article(String title, String fullText) {
        this.title = title;
        this.fullText = fullText;
        this.createDateAndTime = Timestamp.valueOf(LocalDateTime.now());
        this.views = 0;
    }

    public Article(ArticleServiceDTO serviceDTO) {
        this.id =serviceDTO.getId();
        this.title = serviceDTO.getTitle();
        this.fullText = serviceDTO.getFullText();
        this.setCreateDateAndTime(Timestamp.valueOf(LocalDateTime.now()));
    }
}
