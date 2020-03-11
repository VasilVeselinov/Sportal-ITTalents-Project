package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sportal.controller.models.user.UserLoginModel;
import sportal.controller.models.video.VideoModel;
import sportal.exception.BadRequestException;
import sportal.model.service.IVideoService;
import sportal.model.service.dto.VideoServiceDTO;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Positive;

import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_ADMIN;
import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

@RestController
@RequestMapping("/videos")
public class VideoController extends AbstractController {

    @Autowired
    private IVideoService videoService;

    @PostMapping(value = "/upload")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<Void> uploadVideo(
            @RequestPart(value = "video") MultipartFile multipartFile) throws BadRequestException {
        this.videoService.upload(multipartFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/videos/all/article_id_is_null");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/delete/{" + VIDEO_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public VideoModel deleteVideo(
            @PathVariable(name = VIDEO_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long videoId) {
        VideoServiceDTO serviceDTO = this.videoService.delete(videoId);
        return new VideoModel(serviceDTO.getId(), serviceDTO.getUrlOFVideo(), serviceDTO.getArticleId());
    }

    @PutMapping(value = "/add_into_article/{" + VIDEO_ID + "}/{" + ARTICLE_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<Void> addArticleIdByVideoId(
            @PathVariable(name = VIDEO_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long videoId,
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.videoService.addVideoToTheArticleById(videoId, articleId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/all/article_id_is_null")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public List<VideoModel> allPictureWhereArticleIdIsNull() {
        return VideoModel.fromDTOToModel(this.videoService.findAllWhereArticleIdIsNull());
    }
}
