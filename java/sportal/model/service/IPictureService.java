package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.PictureServiceDTO;

import javax.transaction.Transactional;
import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_ADMIN;
import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface IPictureService {

    String THE_PICTURES_DO_NOT_EXIST = "The pictures do not exist!";
    String WRONG_REQUEST = "Invalid request!";

    @Transactional
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    void upload(List<MultipartFile> multipartFiles) throws BadRequestException;

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    PictureServiceDTO delete(long pictureId);

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    void addPictureToTheArticleById(long pictureId, long articleId);

    List<PictureServiceDTO> findAllByArticleIdIsNullAndCheckIsValid(List<PictureServiceDTO> pictures);

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    List<PictureServiceDTO> findAllWhereArticleIdIsNull();

    List<PictureServiceDTO> findAllByArticleId(long articleId);
}
