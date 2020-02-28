package sportal.model.service.dto;

import lombok.Getter;
import lombok.Setter;
import sportal.controller.models.comment.CommentCreateModel;
import sportal.controller.models.comment.CommentEditModel;
import sportal.model.db.pojo.Comment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentServiceDTO {

    private long id;
    private String text;
    private Timestamp datePublished;
    private long userId;
    private long articleId;
    private String userName;
    private int numberOfLikes;
    private int numberOfDislike;

    public CommentServiceDTO(CommentCreateModel commentModel) {
        this.text = commentModel.getCommentText();
        this.articleId = commentModel.getArticleId();
    }

    public CommentServiceDTO(CommentEditModel commentModel) {
        this.id = commentModel.getOldCommentId();
        this.text = commentModel.getNewTextOfComment();
    }

    public CommentServiceDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getFullCommentText();
        this.datePublished = comment.getDatePublished();
        this.userId = comment.getUserId();
        this.articleId = comment.getArticleId();
        if (comment.getUserName() == null){
            this.userName = "The user is deleted!";
        }else {
            this.userName = comment.getUserName();
        }
        this.numberOfLikes = comment.getNumberOfLikes();
        this.numberOfDislike = comment.getNumberOfDislike();

    }

    public static List<CommentServiceDTO> fromPOJOToDTO(List<Comment> comments) {
        List<CommentServiceDTO> dtoList = new ArrayList<>();
        for (Comment comment : comments) {
            dtoList.add(new CommentServiceDTO(comment));
        }
        return dtoList;
    }
}
