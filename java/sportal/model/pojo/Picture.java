package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.picture.PictureDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Picture {

    private long id;
    private String urlOFPicture;
    private long articleID;

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
