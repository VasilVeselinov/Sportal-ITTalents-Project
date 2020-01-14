package sportal.model.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDTO {

    private long id;
    private String fullCommentText;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime datePublished;
    private String userName;
    private int numberOfLikes;
    private int numberOfDislike;

    public CommentResponseDTO(Comment comment) {
        this.setId(comment.getId());
        this.setFullCommentText(comment.getFullCommentText());
        this.setDatePublished(comment.getDatePublished().toLocalDateTime());
        if (comment.getUserName() == null) {
            this.setUserName("User is deleted!");
        } else {
            this.setUserName(comment.getUserName());
        }
        this.setNumberOfLikes(comment.getNumberOfLikes());
        this.setNumberOfDislike(comment.getNumberOfDislike());
    }

    public static List<CommentResponseDTO> fromCommentToCommentResponseDTO(List<Comment> comments) {
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponseDTOList.add(new CommentResponseDTO(comment));
        }
        return commentResponseDTOList;
    }
}
