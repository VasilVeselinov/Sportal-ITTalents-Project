package sportal.model.pojo;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.comment.CommentCreateDTO;
import sportal.model.dto.comment.CommentEditDTO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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

    public Comment(CommentCreateDTO commentCreateDTO, long userId, long articleId) {
        this.setFullCommentText(commentCreateDTO.getCommentText());
        this.setUserId(userId);
        this.setArticleId(articleId);
        this.setDatePublished(Timestamp.valueOf(LocalDateTime.now()));
    }
}
