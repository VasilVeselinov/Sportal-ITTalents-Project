package sportal.model.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.NotExistsObjectException;
import sportal.exception.InvalidInputException;
import sportal.model.db.pojo.Video;
import sportal.model.db.repository.VideoRepository;
import sportal.model.file.FileManagerDAO;
import sportal.model.service.IArticleService;
import sportal.model.service.IVideoService;
import sportal.model.service.dto.VideoServiceDTO;
import sportal.model.validators.VideoValidator;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

import static sportal.util.GlobalConstants.*;

@Service
public class VideoServiceImpl implements IVideoService {

    // Vasko : please fix me, if you change directory for upload videos
    private static final String PATH_NAME =
            System.getProperty("user.home") + "\\Desktop\\" + PACKAGE_FOR_VIDEOS + "\\";
    private static final String NOT_EXIST = "The video do not exist!";
    private static final String DO_NOT_FREE = "This video do not free!";

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private IArticleService articleService;
    private static final Logger LOGGER = LogManager.getLogger(IVideoService.class);

    @Override
    public void upload(MultipartFile multipartFile) throws BadRequestException {
        File fileCreateDirectory = new File(PATH_NAME);
        if (!fileCreateDirectory.exists()) {
            fileCreateDirectory.mkdir();
        }
        Video video = VideoValidator.checkForValidContentType(multipartFile);
        LOGGER.info(SUCCESSFUL_VALIDATION);
        FileManagerDAO fileManagerDAO = new FileManagerDAO(multipartFile, PATH_NAME, video);
        fileManagerDAO.start();
        this.videoRepository.save(video);
        LOGGER.info(SUCCESSFUL_SAVE_IN_DB);
    }

    @Override
    public VideoServiceDTO delete(long videoId) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXIST));
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.videoRepository.deleteById(videoId);
        LOGGER.info(SUCCESSFUL_DELETE_FROM_DB);
        File fileForDelete = new File(PATH_NAME + video.getUrlOfVideo());
        fileForDelete.delete();
        LOGGER.info(SUCCESSFUL_DELETE_OF_FILES);
        return new VideoServiceDTO(video.getId(), video.getUrlOfVideo(), video.getArticleId());
    }

    @Override
    public void addVideoToTheArticleById(long videoId, long articleId, long userId) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXIST));
        if (video.getArticleId() != null) {
            throw new InvalidInputException(DO_NOT_FREE);
        }
        this.articleService.findByIdAndCheckForAuthorCopyright(articleId, userId);
        LOGGER.info(SUCCESSFUL_VALIDATION);
        video.setArticleId(articleId);
        this.videoRepository.save(video);
        LOGGER.info(SUCCESSFUL_UPDATE_OF_DB);
    }

    @Override
    public List<VideoServiceDTO> findAllWhereArticleIdIsNull() {
        List<Video> videos = this.videoRepository.findAllByArticleIdIsNull();
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        return VideoServiceDTO.fromPOJOToDTO(videos);
    }

    @Override
    public List<VideoServiceDTO> findAllByArticleId(long articleId) {
        return new ArrayList<>(VideoServiceDTO.fromPOJOToDTO(this.videoRepository.findAllByArticleId(articleId)));
    }

    @Override
    public void deleteAllWhereArticleIdIsNull() {
        List<Video> videos = this.videoRepository.findAllByArticleIdIsNull();
        this.videoRepository.deleteAll(videos);
        LOGGER.info(SUCCESSFUL_DELETE_FROM_DB);
        for (Video video : videos) {
            File fileForDelete = new File(PATH_NAME + video.getUrlOfVideo());
            fileForDelete.delete();
        }
        LOGGER.info(SUCCESSFUL_DELETE_OF_FILES);
    }
}
