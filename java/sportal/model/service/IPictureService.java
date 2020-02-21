package sportal.model.service;

import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.PictureServiceDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface IPictureService {
    @Transactional
    void upload(List<MultipartFile> multipartFiles, User user) throws BadRequestException;

    PictureServiceDTO delete(long pictureId, User user) throws BadRequestException;

    void addPictureToTheArticleById(long pictureId, long articleId,
                                    User user) throws BadRequestException;

    List<PictureServiceDTO> findAllByArticleIdIsNullAndCheckIsValid(List<PictureServiceDTO> pictures);

    List<PictureServiceDTO> findAllWhereArticleIdIsNull(User user);

    List<PictureServiceDTO> findAllByArticleId(long articleId);
}
