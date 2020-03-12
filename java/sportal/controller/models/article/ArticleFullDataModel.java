package sportal.controller.models.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.models.category.CategoryResponseModel;
import sportal.controller.models.picture.PictureModel;
import sportal.controller.models.video.VideoModel;
import sportal.model.service.dto.ArticleServiceDTO;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleFullDataModel {

    private long id;
    private String title;
    private String fullText;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createDateAndTime;
    private int views;
    private int numberOfLikes;
    private List<CategoryResponseModel> categories;
    private List<PictureModel> pictures;
    private List<VideoModel> videos;
    private String authorName;

    public ArticleFullDataModel(ArticleServiceDTO serviceDTO) {
        this.id = serviceDTO.getId();
        this.title = serviceDTO.getTitle();
        this.fullText = serviceDTO.getFullText();
        this.createDateAndTime = serviceDTO.getCreateDateAndTime().toLocalDateTime();
        this.views = serviceDTO.getViews();
        this.authorName = serviceDTO.getAuthorName();
        this.numberOfLikes = serviceDTO.getNumberOfLikes();
        this.categories = CategoryResponseModel.fromDTOToModel(serviceDTO.getCategories());
        this.pictures = PictureModel.fromDTOToModel(serviceDTO.getPictures());
        this.videos = VideoModel.fromDTOToModel(serviceDTO.getVideos());
    }
}
