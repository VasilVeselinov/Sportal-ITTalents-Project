package sportal.controller.models.video;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.VideoServiceDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class VideoModel {

    private long id;
    private String urlOFVideo;
    private Long articleId;

    public VideoModel(long id, String urlOFVideo, Long articleId) {
        this.id = id;
        this.urlOFVideo = urlOFVideo;
        this.articleId = articleId;
    }

    public static List<VideoModel> fromDTOToModel(List<VideoServiceDTO> videos) {
        List<VideoModel> list = new ArrayList<>();
        for (VideoServiceDTO video: videos){
            list.add(new VideoModel(video.getId(), video.getUrlOFVideo(), video.getArticleId()));
        }
        return list;
    }
}
