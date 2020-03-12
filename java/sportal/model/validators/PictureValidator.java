package sportal.model.validators;

import org.springframework.web.multipart.MultipartFile;
import sportal.exception.ExistsObjectException;
import sportal.exception.InvalidInputException;
import sportal.model.db.pojo.Picture;
import sportal.model.service.dto.PictureServiceDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PictureValidator {

    // date time formatter
    private static final String DATE_AND_TIME_OF_UPLOAD = "date_and_time_of_upload_";
    private static final List<String> CONTENT_TYPES_LIST = Arrays.asList(
            "image/png", "image/jpeg", "image/jpg", "image/gif", "application/octet-stream", "image/bmp", "image/cgm",
            "image/svg+xml", "image/ief", "image/tiff", "image/vnd.djvu", "image/vnd.wap.wbmp", "image/x-cmu-raster",
            "image/x-icon", "image/x-portable-anymap", "image/x-portable-bitmap", "image/x-portable-graymap",
            "image/x-portable-pixmap", "image/x-rgb");
    private static final String FILE_EXPANSION = ".jpg";

    private static final String SOME_OF_THE_PICTURES_DO_NOT_EXIST =
            "Some of the pictures do not exist or do not free!";
    private static final String INVALID_FORMAT = "Invalid format for picture!";

    public static List<PictureServiceDTO> conformityCheck(List<Picture> existsPictures,
                                                          List<PictureServiceDTO> pictures) {
        int countValidPicture = 0;
        for (PictureServiceDTO pictureDTO : pictures) {
            for (Picture picture : existsPictures) {
                if (pictureDTO.getId() == picture.getId()) {
                    countValidPicture++;
                }
            }
        }
        if (countValidPicture != pictures.size()) {
            throw new ExistsObjectException(SOME_OF_THE_PICTURES_DO_NOT_EXIST);
        }
        return pictures;
    }

    public static List<Picture> checkForValidContentType(
            List<MultipartFile> multipartFiles) {
        List<Picture> pictures = new ArrayList<>();
        for (int i = 0; i < multipartFiles.size(); i++) {
            String fileContentType = multipartFiles.get(i).getContentType();
            if (CONTENT_TYPES_LIST.contains(fileContentType)) {
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss.SSS"));
                String urlOfPicture = i + 1 + DATE_AND_TIME_OF_UPLOAD + now + FILE_EXPANSION;
                Picture picture = new Picture();
                picture.setUrlOfPicture(urlOfPicture);
                pictures.add(picture);
            } else {
                throw new InvalidInputException(INVALID_FORMAT);
            }
        }
        return pictures;
    }
}
