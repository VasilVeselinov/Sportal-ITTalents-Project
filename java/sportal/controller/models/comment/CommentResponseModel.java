package sportal.controller.models.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.CommentServiceDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseModel {

    private long id;
    private String fullCommentText;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime datePublished;
    private String userName;
    private int numberOfLikes;
    private int numberOfDislike;
    private long articleId;

    public CommentResponseModel(CommentServiceDTO serviceDTO) {
        this.id = serviceDTO.getId();
        this.fullCommentText = serviceDTO.getText();
        this.datePublished = serviceDTO.getDatePublished().toLocalDateTime();
        if (serviceDTO.getUserName() == null) {
            this.userName = "User is deleted!";
        } else {
            this.userName = serviceDTO.getUserName();
        }
        this.numberOfLikes = serviceDTO.getNumberOfLikes();
        this.numberOfDislike = serviceDTO.getNumberOfDislike();
        this.articleId = serviceDTO.getArticleId();
    }

    public static List<CommentResponseModel> fromDTOToModel(List<CommentServiceDTO> comments) {
        List<CommentResponseModel> modelList = new ArrayList<>();
        for (CommentServiceDTO comment : comments) {
            modelList.add(new CommentResponseModel(comment));
        }
        return modelList;
    }
}
