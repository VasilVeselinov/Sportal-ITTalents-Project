package sportal.model.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.NotExistsObjectException;
import sportal.model.db.dao.IArticleDAO;
import sportal.model.db.pojo.Article;
import sportal.model.db.pojo.Category;
import sportal.model.db.pojo.Picture;
import sportal.model.db.repository.ArticleRepository;
import sportal.model.service.IArticleService;
import sportal.model.service.ICategoryService;
import sportal.model.service.IPictureService;
import sportal.model.service.IVideoService;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.dto.PictureServiceDTO;
import sportal.model.service.dto.VideoServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.util.GlobalConstants.*;

@Service
public class ArticleServiceImpl implements IArticleService {

    private static final String ALREADY_VOTED = "You have already voted on this article!";
    private static final String YOU_ARE_NOT_AUTHOR = "You are not author of this article!";
    private static final String NOT_EXISTS_ARTICLE = "This article does not exist!";
    private static final String COPYRIGHT = "Sportal holds the copyright of this article.";
    private static final int NUMBER_OF_ARTICLES_OF_PAGE = 2;

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IPictureService pictureService;
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IArticleDAO articleDAO;
    @Autowired
    private ArticleRepository articleRepository;
    private static final Logger LOGGER = LogManager.getLogger(IArticleService.class);

    @Override
    public long addArticle(ArticleServiceDTO serviceDTO, long userId) throws SQLException {
        List<CategoryServiceDTO> categories = this.categoryService.validateCategories(serviceDTO.getCategories());
        List<PictureServiceDTO> pictures = this.pictureService.validatePictures(serviceDTO.getPictures());
        LOGGER.info(SUCCESSFUL_VALIDATION);
        Article article = new Article(serviceDTO.getTitle(), serviceDTO.getFullText());
        article.setAuthorId(userId);
        article = this.articleDAO.addArticle(
                article,
                Picture.fromDTOToPojo(pictures),
                Category.fromDTOToPojo(categories));
        LOGGER.info(SUCCESSFUL_SAVE_IN_DB);
        return article.getId();
    }

    @Override
    public List<ArticleServiceDTO> findByTitleOrCategory(String titleOrCategory, int page) throws SQLException {
        final int theFirstArticleOfThePage = NUMBER_OF_ARTICLES_OF_PAGE * (page - 1);
        List<Article> articles = this.articleDAO.allArticlesByTitleOrCategory(
                titleOrCategory,
                NUMBER_OF_ARTICLES_OF_PAGE,
                theFirstArticleOfThePage);
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        return ArticleServiceDTO.fromPOJOToDTO(articles);
    }

    @Override
    public ArticleServiceDTO findById(long articleId) throws SQLException {
        Article article = this.articleDAO.findById(articleId);
        if (article.getId() == 0) {
            throw new NotExistsObjectException(NOT_EXISTS_ARTICLE);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        List<CategoryServiceDTO> categories = this.categoryService.findAllByArticleId(article.getId());
        List<PictureServiceDTO> pictures = this.pictureService.findAllByArticleId(article.getId());
        List<VideoServiceDTO> videos = this.videoService.findAllByArticleId(article.getId());
        ArticleServiceDTO viewArticle = new ArticleServiceDTO(article, pictures, categories, videos);
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        if (article.getAuthorName() == null) {
            viewArticle.setAuthorName(COPYRIGHT);
        } else {
            viewArticle.setAuthorName(article.getAuthorName());
        }
        this.articleDAO.addViewOfByArticleId(article.getId());
        LOGGER.info(SUCCESSFUL_UPDATE_OF_DB);
        return viewArticle;
    }

    @Override
    public List<ArticleServiceDTO> findByCategoryId(long categoryId, int page) throws SQLException {
        final int theFirstArticleOfThePage = NUMBER_OF_ARTICLES_OF_PAGE * (page - 1);
        List<Article> articles = this.articleDAO.articlesByCategoryId(
                categoryId,
                NUMBER_OF_ARTICLES_OF_PAGE,
                theFirstArticleOfThePage);
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        return ArticleServiceDTO.fromPOJOToDTO(articles);
    }

    @Override
    public List<ArticleServiceDTO> findTopFiveReadToday() throws SQLException {
        List<Article> articles = this.articleDAO.topFiveMostViewedArticlesForToday();
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        return ArticleServiceDTO.fromPOJOToDTO(articles);
    }

    @Override
    public long edit(ArticleServiceDTO serviceDTO, long userId) {
        Article existsArticle = this.articleRepository.findById(serviceDTO.getId())
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_ARTICLE));
        this.checkForAuthorCopyright(existsArticle.getAuthorId(), userId);
        LOGGER.info(SUCCESSFUL_VALIDATION);
        Article article = new Article(serviceDTO.getId(), serviceDTO.getTitle(), serviceDTO.getFullText());
        article.setAuthorId(userId);
        article.setViews(existsArticle.getViews());
        long articleId = this.articleRepository.save(article).getId();
        LOGGER.info(SUCCESSFUL_UPDATE_OF_DB);
        return articleId;
    }

    @Override
    public ArticleServiceDTO delete(long articleId) {
        Article existsArticle = this.articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_ARTICLE));
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.articleRepository.deleteById(existsArticle.getId());
        LOGGER.info(SUCCESSFUL_DELETE_FROM_DB);
        return new ArticleServiceDTO(existsArticle);
    }

    @Override
    public void existsById(long articleId) {
        this.articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_ARTICLE));
    }

    @Override
    public void findByIdAndCheckForAuthorCopyright(long articleId, long userId) {
        Article existsArticle = this.articleRepository.findById(articleId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_ARTICLE));
        this.checkForAuthorCopyright(existsArticle.getAuthorId(), userId);
    }

    private void checkForAuthorCopyright(long authorId, long userId) {
        if (authorId != userId) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
    }

    @Override
    public void isArticleLikedByUser(long articleId, long userId) throws BadRequestException {
        if (this.articleDAO.isArticleLikedByUser(articleId, userId)) {
            throw new BadRequestException(ALREADY_VOTED);
        }
    }
}
