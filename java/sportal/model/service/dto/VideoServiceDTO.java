package sportal.model.service.dto;

import lombok.Getter;
import lombok.Setter;
import sportal.model.db.pojo.Video;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VideoServiceDTO {

    private long id;
    private String urlOFVideo;
    private Long articleId;

    public VideoServiceDTO(long id, String urlOfVideo, Long articleId) {
        this.id = id;
        this.urlOFVideo = urlOfVideo;
        this.articleId = articleId;
    }

    public static List<VideoServiceDTO> fromPOJOToDTO(List<Video> videos) {
        List<VideoServiceDTO> list = new ArrayList<>();
        for (Video video : videos) {
            list.add(new VideoServiceDTO(video.getId(), video.getUrlOfVideo(), video.getArticleId()));
        }
        return list;
    }
}
