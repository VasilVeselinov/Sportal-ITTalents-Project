package sportal.model.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.NotExistsObjectException;
import sportal.exception.InvalidInputException;
import sportal.model.file.FileManagerDAO;
import sportal.model.service.IArticleService;
import sportal.model.validators.PictureValidator;
import sportal.model.db.pojo.Picture;
import sportal.model.db.repository.PictureRepository;
import sportal.model.service.IPictureService;
import sportal.model.service.dto.PictureServiceDTO;

import javax.transaction.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static sportal.util.GlobalConstants.*;

@Service
public class PictureServiceImpl implements IPictureService {
    // Vasko : please fix me, if you change directory for upload pictures
    private static final String PATH_NAME =
            System.getProperty("user.home") + "\\Desktop\\" + PACKAGE_FOR_PICTURES + "\\";
    private static final String NOT_EXIST = "The picture do not exist!";
    private static final String DO_NOT_FREE = "The picture do not free!";
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private IArticleService articleService;
    private static final Logger LOGGER = LogManager.getLogger(IPictureService.class);

    @Transactional
    @Override
    public void upload(List<MultipartFile> multipartFiles) throws BadRequestException {
        File fileCreateDirectory = new File(PATH_NAME);
        if (!fileCreateDirectory.exists()) {
            fileCreateDirectory.mkdir();
        }
        List<Picture> pictures = PictureValidator.checkForValidContentType(multipartFiles);
        LOGGER.info(SUCCESSFUL_VALIDATION);
        FileManagerDAO fileManagerDAO = new FileManagerDAO(multipartFiles, PATH_NAME, pictures);
        fileManagerDAO.start();
        this.pictureRepository.saveAll(pictures);
        LOGGER.info(SUCCESSFUL_SAVE_IN_DB);
    }

    @Override
    public PictureServiceDTO delete(long pictureId) {
        Picture picture = this.pictureRepository.findById(pictureId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXIST));
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.pictureRepository.deleteById(pictureId);
        LOGGER.info(SUCCESSFUL_DELETE_FROM_DB);
        File fileForDelete = new File(PATH_NAME + picture.getUrlOfPicture());
        fileForDelete.delete();
        LOGGER.info(SUCCESSFUL_DELETE_OF_FILES);
        return new PictureServiceDTO(picture);
    }

    @Override
    public void addPictureToTheArticleById(long pictureId, long articleId, long userId) {
        Picture picture = this.pictureRepository.findById(pictureId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXIST));
        if (picture.getArticleId() != null) {
            throw new InvalidInputException(DO_NOT_FREE);
        }
        this.articleService.findByIdAndCheckForAuthorCopyright(articleId, userId);
        LOGGER.info(SUCCESSFUL_VALIDATION);
        picture.setArticleId(articleId);
        this.pictureRepository.save(picture);
        LOGGER.info(SUCCESSFUL_UPDATE_OF_DB);
    }

    @Override
    public List<PictureServiceDTO> validatePictures(List<PictureServiceDTO> pictures) {
        List<Picture> pictureList = this.pictureRepository.findAllByArticleIdIsNull();
        return PictureValidator.conformityCheck(pictureList, pictures);
    }

    @Override
    public List<PictureServiceDTO> findAllWhereArticleIdIsNull() {
        List<Picture> pictures = this.pictureRepository.findAllByArticleIdIsNull();
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        return PictureServiceDTO.fromPOJOToDTO(pictures);
    }

    @Override
    public List<PictureServiceDTO> findAllByArticleId(long articleId) {
        return new ArrayList<>(PictureServiceDTO.fromPOJOToDTO(this.pictureRepository.findAllByArticleId(articleId)));
    }

    @Override
    public void deleteAllWhereArticleIdIsNull() {
        List<Picture> pictures = this.pictureRepository.findAllByArticleIdIsNull();
        this.pictureRepository.deleteAll(pictures);
        LOGGER.info(SUCCESSFUL_DELETE_FROM_DB);
        for (Picture picture : pictures) {
            File fileForDelete = new File(PATH_NAME + picture.getUrlOfPicture());
            fileForDelete.delete();
        }
        LOGGER.info(SUCCESSFUL_DELETE_OF_FILES);
    }
}
