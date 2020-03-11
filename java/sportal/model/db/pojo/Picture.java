package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.PictureServiceDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pictures")
public class Picture extends BasePOJO {

    @Column(name = "picture_url")
    private String urlOfPicture;
    private Long articleId;

    private Picture(long id) {
        this.setId(id);
    }

    public static List<Picture> fromDTOToPojo(List<PictureServiceDTO> pictures) {
        List<Picture> pojoList = new ArrayList<>();
        for (PictureServiceDTO picture : pictures) {
            pojoList.add(new Picture(picture.getId()));
        }
        return pojoList;
    }
}
