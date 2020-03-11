package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.VideoServiceDTO;

import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_ADMIN;
import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface IVideoService {

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    void upload(MultipartFile multipartFile) throws BadRequestException;

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    VideoServiceDTO delete(long videoId);

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    void addVideoToTheArticleById(long videoId, long articleId, long userId);

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    List<VideoServiceDTO> findAllWhereArticleIdIsNull();

    List<VideoServiceDTO> findAllByArticleId(long articleId);
}

