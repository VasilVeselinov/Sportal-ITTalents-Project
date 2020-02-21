package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.dto.PictureServiceDTO;

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

    public Picture(long id) {
        this.id = id;
    }

    public static List<Picture> fromDTOToPojo(List<PictureServiceDTO> pictures) {
        List<Picture> pojoList = new ArrayList<>();
        for (PictureServiceDTO picture : pictures) {
            pojoList.add(new Picture(picture.getId()));
        }
        return pojoList;
    }
}
