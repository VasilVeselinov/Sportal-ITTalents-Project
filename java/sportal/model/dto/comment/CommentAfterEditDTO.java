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
public class CommentAfterEditDTO {

    private long id;
    private String newFullCommentText;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime datePublished;
    private String userName;

    public CommentAfterEditDTO(Comment editComment) {
        this.setId(editComment.getId());
        this.setNewFullCommentText(editComment.getFullCommentText());
        this.setDatePublished(editComment.getDatePublished().toLocalDateTime());
        this.setUserName(editComment.getUserName());
    }
}
