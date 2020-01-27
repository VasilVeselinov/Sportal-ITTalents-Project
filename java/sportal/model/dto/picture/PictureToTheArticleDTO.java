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
public class PictureToTheArticleDTO {

    private long id;
    private String urlOFPicture;

    private PictureToTheArticleDTO(Picture picture) {
        this.setId(picture.getId());
        this.setUrlOFPicture(picture.getUrlOFPicture());
    }

    public static List<PictureToTheArticleDTO> fromPictureToPictureToTheArticleDTO(List<Picture> pictures) {
        List<PictureToTheArticleDTO> list = new ArrayList<>();
        for (Picture picture: pictures){
            list.add(new PictureToTheArticleDTO(picture));
        }
        return list;
    }
}
