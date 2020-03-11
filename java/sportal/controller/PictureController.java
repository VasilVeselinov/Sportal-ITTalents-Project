package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.controller.models.user.UserLoginModel;
import sportal.exception.BadRequestException;
import sportal.controller.models.picture.PictureModel;
import sportal.model.service.IPictureService;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Positive;
import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_ADMIN;
import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

@RestController
@RequestMapping("/pictures")
public class PictureController extends AbstractController {

    @Autowired
    private IPictureService pictureService;

    @PostMapping(value = "/upload")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<Void> uploadPictures(
            @RequestPart(value = "picture") List<MultipartFile> multipartFiles) throws BadRequestException {
        this.pictureService.upload(multipartFiles);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/pictures/all/article_id_is_null");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/delete/{" + PICTURE_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public PictureModel deletePicture(
            @PathVariable(name = PICTURE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long pictureId) {
        return new PictureModel(this.pictureService.delete(pictureId));
    }

    @PutMapping(value = "/add_into_article/{" + PICTURE_ID + "}/{" + ARTICLE_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<Void> addArticleIdByPictureId(
            @PathVariable(name = PICTURE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long pictureId,
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.pictureService.addPictureToTheArticleById(pictureId, articleId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/all/article_id_is_null")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public List<PictureModel> allPictureWhereArticleIdIsNull() {
        return PictureModel.fromDTOToModel(this.pictureService.findAllWhereArticleIdIsNull());
    }
}
