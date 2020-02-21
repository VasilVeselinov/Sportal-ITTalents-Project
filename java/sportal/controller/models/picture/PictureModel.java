package sportal.controller.models.picture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.PictureServiceDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PictureModel {

    private long id;
    private String urlOFPicture;
    private Long articleId;

    public PictureModel(PictureServiceDTO serviceDTO) {
        this.id = serviceDTO.getId();
        this.urlOFPicture = serviceDTO.getUrlOFPicture();
        this.articleId = serviceDTO.getArticleId();
    }

    public static List<PictureModel> fromDTOToModel(List<PictureServiceDTO> pictures) {
        List<PictureModel> list = new ArrayList<>();
        for (PictureServiceDTO picture: pictures){
            list.add(new PictureModel(picture));
        }
        return list;
    }
}
