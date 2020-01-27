package sportal.model.data_validators;

import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.pojo.Picture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sportal.controller.AbstractController.WRONG_REQUEST;

public class PictureValidator extends AbstractValidator{

    private static final String FILE_EXPANSION = ".jpg";
    // date time formatter
    private static final String DATE_AND_TIME_OF_UPLOAD = "date_and_time_of_upload_";
    private static final List<String> CONTENT_TYPES_LIST = Arrays.asList(
            "image/png", "image/jpeg", "image/gif", "application/octet-stream", "image/bmp", "image/cgm",
            "image/svg+xml", "image/ief", "image/tiff", "image/vnd.djvu", "image/vnd.wap.wbmp", "image/x-cmu-raster",
            "image/x-icon", "image/x-portable-anymap", "image/x-portable-bitmap", "image/x-portable-graymap",
            "image/x-portable-pixmap", "image/x-rgb");

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

    public static List<Picture> checkForValidContentType(List<MultipartFile> multipartFiles) throws BadRequestException {
        List<Picture> pictures = new ArrayList<>();
        for (int i = 0; i < multipartFiles.size(); i++) {
            String fileContentType = multipartFiles.get(i).getContentType();
            if (CONTENT_TYPES_LIST.contains(fileContentType)) {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss.SSS"));
                String urlOfPicture = i + DATE_AND_TIME_OF_UPLOAD + now + FILE_EXPANSION;
                Picture picture = new Picture();
                picture.setUrlOFPicture(urlOfPicture);
                pictures.add(picture);
            } else {
                throw new BadRequestException(WRONG_REQUEST);
            }
        }
        return pictures;
    }
}
