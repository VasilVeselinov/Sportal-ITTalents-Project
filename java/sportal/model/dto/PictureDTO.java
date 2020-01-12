package sportal.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Picture;

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
}
