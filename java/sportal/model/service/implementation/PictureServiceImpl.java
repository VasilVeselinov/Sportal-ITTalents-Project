package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
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

import static sportal.GlobalConstants.PACKAGE_FOR_PICTURES;

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

    @Transactional
    @Override
    public void upload(List<MultipartFile> multipartFiles) throws BadRequestException {
        File fileCreateDirectory = new File(PATH_NAME);
        if (!fileCreateDirectory.exists()) {
            fileCreateDirectory.mkdir();
        }
        List<Picture> pictures = PictureValidator.checkForValidContentType(multipartFiles);
        FileManagerDAO fileManagerDAO = new FileManagerDAO(multipartFiles, PATH_NAME, pictures);
        fileManagerDAO.start();
        this.pictureRepository.saveAll(pictures);
    }

    @Override
    public PictureServiceDTO delete(long pictureId) {
        Picture picture = this.pictureRepository.findById(pictureId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXIST));
        this.pictureRepository.deleteById(pictureId);
        File fileForDelete = new File(PATH_NAME + picture.getUrlOfPicture());
        fileForDelete.delete();
        return new PictureServiceDTO(picture);
    }

    @Override
    public void addPictureToTheArticleById(long pictureId, long articleId, long userId) {
        Picture picture = this.pictureRepository.findById(pictureId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXIST));
        if (picture.getArticleId() != null) {
            throw new ExistsObjectException(DO_NOT_FREE);
        }
        this.articleService.findByIdAndCheckForAuthorCopyright(articleId, userId);
        picture.setArticleId(articleId);
        this.pictureRepository.save(picture);
    }

    @Override
    public List<PictureServiceDTO> findAllByArticleIdIsNullAndCheckIsValid(List<PictureServiceDTO> pictures) {
        List<Picture> pictureList = this.findAllByArticleIdIsNull();
        return PictureValidator.conformityCheck(pictureList, pictures);
    }

    private List<Picture> findAllByArticleIdIsNull() {
        return this.pictureRepository.findAllByArticleIdIsNull();
    }

    @Override
    public List<PictureServiceDTO> findAllWhereArticleIdIsNull() {
        return PictureServiceDTO.fromPOJOToDTO(this.pictureRepository.findAllByArticleIdIsNull());
    }

    @Override
    public List<PictureServiceDTO> findAllByArticleId(long articleId) {
        return new ArrayList<>(
                PictureServiceDTO.fromPOJOToDTO(this.pictureRepository.findAllByArticleId(articleId)));
    }
}
