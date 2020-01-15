package sportal.model.data_validators;

import sportal.exception.ExistsObjectException;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.pojo.Picture;

import java.util.List;

public class PictureValidator extends AbstractValidator{

    public static List<Picture> conformityCheck(List<Picture> existsPictures, List<PictureDTO> pictures) {
        int countValidPicture = 0;
        for (PictureDTO pictureDTO : pictures){
            for (Picture picture : existsPictures){
                if (pictureDTO.getId() == picture.getId()){
                    countValidPicture++;
                }
            }
        }
        if (countValidPicture != pictures.size()){
            throw new ExistsObjectException(SOME_OF_THE_PICTURES_DO_NOT_EXIST);
        }
        return Picture.fromPictureDTOToPicture(pictures);
    }
}
