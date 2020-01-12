package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.comment.CommentCreateDTO;
import sportal.model.dto.comment.CommentEditDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Comment {

    private long id;
    private String fullCommentText;
    private Timestamp datePublished;
    private long userId;
    private long articleId;
    private String userName;
    private int numberOfLikes;
    private int numberOfDislike;

    public Comment(CommentCreateDTO commentCreateDTO, long userId, long articleId) {
        this.setFullCommentText(commentCreateDTO.getCommentText());
        this.setUserId(userId);
        this.setArticleId(articleId);
        this.setDatePublished(Timestamp.valueOf(LocalDateTime.now()));
    }

    public Comment(CommentEditDTO validComment) {
        this.setId(validComment.getId());
        this.setFullCommentText(validComment.getNewTextOfComment());
    }
}
