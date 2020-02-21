package sportal.model.service.dto;

import lombok.Getter;
import lombok.Setter;
import sportal.controller.models.picture.PictureModel;
import sportal.model.db.pojo.Picture;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PictureServiceDTO {

    private long id;
    private String urlOFPicture;
    private Long articleId;

    private PictureServiceDTO(long id) {
        this.id = id;
    }

    public PictureServiceDTO(Picture picture) {
        this(picture.getId());
        this.urlOFPicture = picture.getUrlOFPicture();
        this.articleId = picture.getArticleId();
    }

    public static List<PictureServiceDTO> fromPOJOToDTO(List<Picture> pictures) {
        List<PictureServiceDTO> dtoList = new ArrayList<>();
        for (Picture picture : pictures) {
            dtoList.add(new PictureServiceDTO(picture));
        }
        return dtoList;
    }

    public static List<PictureServiceDTO> fromModelToDTO(List<PictureModel> pictures) {
        List<PictureServiceDTO> dtoList = new ArrayList<>();
        for (PictureModel picture : pictures) {
            dtoList.add(new PictureServiceDTO(picture.getId()));
        }
        return dtoList;
    }
}
