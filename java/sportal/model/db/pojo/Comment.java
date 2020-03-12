package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment extends BasePOJO {

    private String fullCommentText;
    private Timestamp datePublished;
    private long userId;
    private long articleId;
    @Transient
    private String userName;
    @Transient
    private int numberOfLikes;
    @Transient
    private int numberOfDislike;

    public Comment(String text, long articleId , long userId) {
        this.setFullCommentText(text);
        this.setUserId(userId);
        this.setArticleId(articleId);
        this.setDatePublished(Timestamp.valueOf(LocalDateTime.now()));
    }
}
