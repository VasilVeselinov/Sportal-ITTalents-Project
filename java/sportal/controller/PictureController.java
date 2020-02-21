package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.controller.models.picture.PictureModel;
import sportal.model.pojo.User;
import sportal.model.service.IPictureService;
import sportal.model.service.dto.PictureServiceDTO;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/pictures")
public class PictureController extends AbstractController {

    @Autowired
    private IPictureService pictureService;

    @PostMapping(value = "/upload")
    public List<PictureModel> uploadPictures(@RequestPart(value = "picture") List<MultipartFile> multipartFiles,
                                             HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        List<PictureServiceDTO> picturesAfterInsertInDB = this.pictureService.upload(multipartFiles, user);
        return PictureModel.fromServiceDTOToModel(picturesAfterInsertInDB);
    }

    @DeleteMapping(value = "/delete/{" + PICTURE_ID + "}")
    public PictureModel deletePicture(@PathVariable(name = PICTURE_ID) long pictureId,
                                      HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new PictureModel(this.pictureService.delete(pictureId, user));
    }

    @PutMapping(value = "/add_into_article/{" + PICTURE_ID + "}/{" + ARTICLE_ID + "}")
    public PictureModel addArticleIdByPictureId(
            @PathVariable(name = PICTURE_ID) long pictureId,
            @PathVariable(name = ARTICLE_ID) long articleId,
            HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new PictureModel(this.pictureService.addPictureToTheArticleById(pictureId, articleId, user));
    }
}
