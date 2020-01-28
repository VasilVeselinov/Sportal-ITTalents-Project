package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.pojo.Picture;
import sportal.model.service.PictureService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/pictures")
public class PictureController extends AbstractController {

    @Autowired
    private PictureService pictureService;

    @PostMapping(value = "/upload")
    public List<PictureDTO> uploadPictures(@RequestPart(value = "picture") List<MultipartFile> multipartFiles,
                                           HttpSession session) throws BadRequestException {
        List<Picture> picturesAfterInsertInDB = this.pictureService.upload(multipartFiles, session);
        return PictureDTO.fromPictureToPictureDTO(picturesAfterInsertInDB);
    }

    @DeleteMapping(value = "/delete/{" + PICTURE_ID + "}")
    public PictureDTO deletePicture(@PathVariable(name = PICTURE_ID) long pictureId,
                                    HttpSession session) throws BadRequestException {
        return this.pictureService.delete(pictureId, session);
    }

    @PutMapping(value = "/add_into_article/{" + PICTURE_ID + "}/{" + ARTICLE_ID + "}")
    public PictureDTO addArticleIdByPictureId(
            @PathVariable(name = PICTURE_ID) long pictureId,
            @PathVariable(name = ARTICLE_ID) long articleId,
            HttpSession session) throws BadRequestException {
        return this.pictureService.addPictureToTheArticleById(pictureId, articleId, session);
    }
}
