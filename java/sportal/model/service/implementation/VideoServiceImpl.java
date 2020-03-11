package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
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

import static sportal.GlobalConstants.WRONG_REQUEST;

@Service
public class VideoServiceImpl implements IVideoService {

    // Vasko : please fix me, if you change directory for upload videos
    private static final String PACKAGE_NAME = System.getProperty("user.home") + "\\Desktop\\uploadVideos\\";
    private static final String NOT_EXIST_VIDEO = "The video do not exist!";
    private static final String DO_NOT_FREE = "This video do not free!";

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private IArticleService articleService;

    @Override
    public void upload(MultipartFile multipartFile) throws BadRequestException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        File fileCreateDirectory = new File(PACKAGE_NAME);
        if (!fileCreateDirectory.exists()) {
            fileCreateDirectory.mkdir();
        }
        Video video = VideoValidator.checkForValidContentType(multipartFile);
        FileManagerDAO fileManagerDAO = new FileManagerDAO(multipartFile, PACKAGE_NAME, video);
        fileManagerDAO.start();
        this.videoRepository.save(video);
    }

    @Override
    public VideoServiceDTO delete(long videoId) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXIST_VIDEO));
        this.videoRepository.deleteById(videoId);
        File fileForDelete = new File(PACKAGE_NAME + video.getUrlOfVideo());
        fileForDelete.delete();
        return new VideoServiceDTO(video.getId(), video.getUrlOfVideo(), video.getArticleId());
    }

    @Override
    public void addVideoToTheArticleById(long videoId, long articleId, long userId) {
        Video video = this.videoRepository.findById(videoId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXIST_VIDEO));
        if (video.getArticleId() != null) {
            throw new ExistsObjectException(DO_NOT_FREE);
        }
        this.articleService.findByIdAndCheckForAuthorCopyright(articleId, userId);
        video.setArticleId(articleId);
        this.videoRepository.save(video);
    }

    @Override
    public List<VideoServiceDTO> findAllWhereArticleIdIsNull() {
        return VideoServiceDTO.fromPOJOToDTO(this.videoRepository.findAllByArticleIdIsNull());
    }

    @Override
    public List<VideoServiceDTO> findAllByArticleId(long articleId) {
        return new ArrayList<>(
                VideoServiceDTO.fromPOJOToDTO(this.videoRepository.findAllByArticleId(articleId)));
    }
}
