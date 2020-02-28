package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.file.FileManagerDAO;
import sportal.model.validators.PictureValidator;
import sportal.model.db.pojo.Picture;
import sportal.model.db.repository.PictureRepository;
import sportal.model.service.IPictureService;
import sportal.model.service.dto.PictureServiceDTO;

import javax.transaction.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void upload(List<MultipartFile> multipartFiles) throws BadRequestException {
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
        this.pictureRepository.saveAll(pictures);
    }

    @Override
    public PictureServiceDTO delete(long pictureId) {
        Optional<Picture> picture = this.pictureRepository.findById(pictureId);
        if (!picture.isPresent()) {
            throw new ExistsObjectException(THE_PICTURES_DO_NOT_EXIST);
        }
        this.pictureRepository.deleteById(pictureId);
        File fileForDelete = new File(PACKAGE_NAME + picture.get().getUrlOFPicture());
        fileForDelete.delete();
        return new PictureServiceDTO(picture.get());
    }

    @Override
    public void addPictureToTheArticleById(long pictureId, long articleId) {
        Optional<Picture> optionalPicture = this.pictureRepository.findById(pictureId);
        Picture validPicture = PictureValidator.checkForValidPicture(optionalPicture);
        this.articleService.existsById(articleId);
        validPicture.setArticleId(articleId);
        this.pictureRepository.save(validPicture);
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
