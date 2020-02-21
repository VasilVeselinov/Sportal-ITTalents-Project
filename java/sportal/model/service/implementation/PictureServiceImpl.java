package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.FileManagerDAO;
import sportal.model.data_validators.PictureValidator;
import sportal.model.data_validators.UserValidator;
import sportal.model.pojo.Picture;
import sportal.model.pojo.User;
import sportal.model.repository.PictureRepository;
import sportal.model.service.IPictureService;
import sportal.model.service.dto.PictureServiceDTO;

import javax.transaction.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.WRONG_REQUEST;
import static sportal.model.data_validators.AbstractValidator.THE_PICTURES_DO_NOT_EXIST;
import static sportal.model.data_validators.AbstractValidator.THIS_ARTICLE_IS_NOT_EXISTS;

@Service
public class PictureServiceImpl implements IPictureService {
    // Vasko : please fix me, if you change directory
    private static final String PACKAGE_NAME = "C:\\Users\\ACER\\Desktop\\uploadPictures\\";
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private ArticleServiceImpl articleService;

    @Transactional
    @Override
    public List<PictureServiceDTO> upload(List<MultipartFile> multipartFiles, User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        File fileCreateDirectory = new File(PACKAGE_NAME);
        if (!fileCreateDirectory.exists()) {
            fileCreateDirectory.mkdir();
        }
        List<Picture> pictures = PictureValidator.checkForValidContentType(multipartFiles);
        FileManagerDAO fileManagerDAO = new FileManagerDAO(multipartFiles, PACKAGE_NAME, pictures);
        fileManagerDAO.start();
        return new ArrayList<>(PictureServiceDTO.fromPOJOToDTO(this.pictureRepository.saveAll(pictures)));
    }

    @Override
    public PictureServiceDTO delete(long pictureId, User user) throws BadRequestException {
        if (pictureId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Picture> picture = this.pictureRepository.findById(pictureId);
        if (!picture.isPresent()) {
            throw new ExistsObjectException(THE_PICTURES_DO_NOT_EXIST);
        }
        this.pictureRepository.deleteById(pictureId);
        File fileForDelete = new File(PACKAGE_NAME + picture.get().getUrlOFPicture());
        fileForDelete.delete();
        return  new PictureServiceDTO(picture.get());
    }

    @Override
    public PictureServiceDTO addPictureToTheArticleById(long pictureId, long articleId,
                                                 User user) throws BadRequestException {
        if (pictureId < 0 || articleId < 0) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Picture> optionalPicture = this.pictureRepository.findById(pictureId);
        Picture validPicture = PictureValidator.checkForValidPicture(optionalPicture);
        if (!this.articleService.existsById(articleId)) {
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        validPicture.setArticleId(articleId);
        Picture updatedPicture = this.pictureRepository.save(validPicture);
        return new PictureServiceDTO(updatedPicture);
    }

    @Override
    public List<PictureServiceDTO> findAllByArticleIdIsNullAndCheckIsValid(List<PictureServiceDTO> pictures) {
        return PictureValidator.conformityCheck(this.pictureRepository.findAllByArticleIdIsNull(), pictures);
    }

    @Override
    public List<PictureServiceDTO> findAllByArticleId(long articleId) {
        return new ArrayList<>(
                PictureServiceDTO.fromPOJOToDTO(this.pictureRepository.findAllByArticleId(articleId)));
    }
}
