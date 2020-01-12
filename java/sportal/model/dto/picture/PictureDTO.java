package sportal.model.dto.picture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Picture;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PictureDTO {

    private long id;
    private String urlOFPicture;

    public PictureDTO(Picture picture) {
        this.setId(picture.getId());
        this.setUrlOFPicture(picture.getUrlOFPicture());
    }

    public static List<PictureDTO> fromPictureToPictureDTO(List<Picture> pictures) {
        List<PictureDTO> list = new ArrayList<>();
        for (Picture picture: pictures){
            list.add(new PictureDTO(picture));
        }
        return list;
    }
}
