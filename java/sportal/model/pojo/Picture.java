package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.picture.PictureDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pictures")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "picture_url")
    private String urlOFPicture;
    private Long articleId;

    public Picture(PictureDTO pictureDTO) {
        this.setId(pictureDTO.getId());
        this.setUrlOFPicture(pictureDTO.getUrlOFPicture());
    }

    public static List<Picture> fromPictureDTOToPicture(List<PictureDTO> pictures) {
        List<Picture> pictureList = new ArrayList<>();
        for (PictureDTO pictureDTO : pictures){
            pictureList.add(new Picture(pictureDTO));
        }
        return pictureList;
    }
}
