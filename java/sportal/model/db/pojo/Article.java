package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.ArticleServiceDTO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article extends BasePOJO {

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
    }

    public Article(ArticleServiceDTO serviceDTO) {
        this.setId(serviceDTO.getId());
        this.title = serviceDTO.getTitle();
        this.fullText = serviceDTO.getFullText();
        this.setCreateDateAndTime(Timestamp.valueOf(LocalDateTime.now()));
    }
}
