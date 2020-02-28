package sportal.model.service;

import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.PictureServiceDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface IPictureService {

    String THE_PICTURES_DO_NOT_EXIST = "The pictures do not exist!";
    String WRONG_REQUEST = "Invalid request!";

    @Transactional
    void upload(List<MultipartFile> multipartFiles) throws BadRequestException;

    PictureServiceDTO delete(long pictureId) throws BadRequestException;

    void addPictureToTheArticleById(long pictureId, long articleId) throws BadRequestException;

    List<PictureServiceDTO> findAllByArticleIdIsNullAndCheckIsValid(List<PictureServiceDTO> pictures);

    List<PictureServiceDTO> findAllWhereArticleIdIsNull();

    List<PictureServiceDTO> findAllByArticleId(long articleId);
}
