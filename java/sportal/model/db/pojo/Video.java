package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "videos")
public class Video extends BasePOJO {

    @Column(name = "video_url")
    private String urlOfVideo;
    private Long articleId;
}
