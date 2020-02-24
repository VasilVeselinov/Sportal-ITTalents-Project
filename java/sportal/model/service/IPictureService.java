package sportal.model.service;

import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.PictureServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface IPictureService {
    @Transactional
    void upload(List<MultipartFile> multipartFiles, UserServiceDTO userOfSession) throws BadRequestException;

    PictureServiceDTO delete(long pictureId, UserServiceDTO userOfSession) throws BadRequestException;

    void addPictureToTheArticleById(long pictureId, long articleId,
                                    UserServiceDTO userOfSession) throws BadRequestException;

    List<PictureServiceDTO> findAllByArticleIdIsNullAndCheckIsValid(List<PictureServiceDTO> pictures);

    List<PictureServiceDTO> findAllWhereArticleIdIsNull(UserServiceDTO userOfSession);

    List<PictureServiceDTO> findAllByArticleId(long articleId);
}
