package sportal.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Comment;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseAfterDeleteDTO {

    private long id;
    private String fullCommentText;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime datePublished;
    private long userId;
    private long articleId;

    public CommentResponseAfterDeleteDTO(Comment existsComment) {
        this.setId(existsComment.getId());
        this.setFullCommentText(existsComment.getFullCommentText());
        this.setDatePublished(existsComment.getDatePublished().toLocalDateTime());
        this.setUserId(existsComment.getUserId());
        this.setArticleId(existsComment.getArticleId());
    }

}
