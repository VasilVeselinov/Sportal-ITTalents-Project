package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.FileManagerDAO;
import sportal.model.dao.PictureDAO;
import sportal.model.data_validators.SessionValidator;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.pojo.Picture;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PictureController extends AbstractController {

    private static final String PACKAGE_NAME = "C:\\Users\\ACER\\Desktop\\uploadPictures\\";
    private static final String FILE_EXPANSION = ".jpg";
    // date time formatter
    private static final String DATE_AND_TIME_OF_UPLOAD = "date_and_time_of_upload_";

    @Autowired
    private PictureDAO pictureDAO;

    @PostMapping(value = "/pictures")
    public List<PictureDTO> uploadPictures(@RequestPart(value = "picture") List<MultipartFile> multipartFile,
                                           HttpSession session) throws SQLException, BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Picture> pictures = new ArrayList<>();
        for (MultipartFile mf : multipartFile) {
            String contentType = mf.getContentType();
            // vasko
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss.SSS"));
            String urlOfPicture = DATE_AND_TIME_OF_UPLOAD + now + FILE_EXPANSION;
            Picture picture = new Picture();
            picture.setUrlOFPicture(urlOfPicture);
            pictures.add(picture);
            FileManagerDAO fileManagerDAO = new FileManagerDAO(mf, PACKAGE_NAME + urlOfPicture);
            fileManagerDAO.start();
        }
        List<Picture> picturesAfterInsertInDB = this.pictureDAO.uploadOfPictures(pictures);
        return PictureDTO.fromPictureToPictureDTO(picturesAfterInsertInDB);
    }

    @DeleteMapping(value = "/pictures/{" + PICTURE_ID + "}")
    public PictureDTO deletePicture(@PathVariable(name = PICTURE_ID) long pictureId,
                                    HttpSession session) throws SQLException, BadRequestException {
        if (pictureId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Picture picture = this.pictureDAO.findPictureById(pictureId);
        if (this.pictureDAO.deletePictureById(pictureId) == 0) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        File fileForDelete = new File(PACKAGE_NAME + picture.getUrlOFPicture());
        fileForDelete.delete();
        return new PictureDTO(picture);
    }
}
