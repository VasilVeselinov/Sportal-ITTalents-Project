package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.controller.models.picture.PictureModel;
import sportal.model.db.pojo.User;
import sportal.model.service.IPictureService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/pictures")
public class PictureController extends AbstractController {

    @Autowired
    private IPictureService pictureService;

    @PostMapping(value = "/upload")
    public ResponseEntity<Void> uploadPictures(@RequestPart(value = "picture") List<MultipartFile> multipartFiles,
                                             HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.pictureService.upload(multipartFiles, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/pictures/all/article_id_is_null");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/delete/{" + PICTURE_ID + "}")
    public PictureModel deletePicture(@PathVariable(name = PICTURE_ID) long pictureId,
                                      HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new PictureModel(this.pictureService.delete(pictureId, user));
    }

    @PutMapping(value = "/add_into_article/{" + PICTURE_ID + "}/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> addArticleIdByPictureId(
            @PathVariable(name = PICTURE_ID) long pictureId,
            @PathVariable(name = ARTICLE_ID) long articleId,
            HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.pictureService.addPictureToTheArticleById(pictureId, articleId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/all/article_id_is_null")
    public List<PictureModel> allPictureWhereArticleIdIsNull(HttpSession session){
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return PictureModel.fromDTOToModel(this.pictureService.findAllWhereArticleIdIsNull(user));
    }
}
