package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
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

@Service
public class ArticleServiceImpl implements IArticleService {

    private static final String ALREADY_VOTED = "You have already voted on this article!";
    private static final String YOU_ARE_NOT_AUTHOR = "You are not author of this article!";
    private static final String NOT_EXISTS_ARTICLE = "This article is not exists!";
    private static final String COPYRIGHT = "Sportal holds the copyright of this article.";

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

    @Override
    public long addArticle(ArticleServiceDTO serviceDTO, long userId) throws SQLException {
        List<CategoryServiceDTO> dtoListOfCategories =
                this.categoryService.findAllExistsCategoriseAndCheckIsValid(serviceDTO.getCategories());
        List<PictureServiceDTO> dtoListOfPictures =
                this.pictureService.findAllByArticleIdIsNullAndCheckIsValid(serviceDTO.getPictures());
        Article article = new Article(serviceDTO.getTitle(), serviceDTO.getFullText());
        article.setAuthorId(userId);
        article = this.articleDAO.addArticle(
                article,
                Picture.fromDTOToPojo(dtoListOfPictures),
                Category.fromDTOToPojo(dtoListOfCategories));
        return article.getId();
    }

    @Override
    public List<ArticleServiceDTO> findByArticleTitleOrCategory(String titleOrCategory) throws SQLException {
        return ArticleServiceDTO.fromPOJOToDTO(this.articleDAO.allArticlesByTitleOrCategory(titleOrCategory));
    }

    @Override
    public ArticleServiceDTO findArticleById(long articleId) throws SQLException {
        Article article = this.articleDAO.findById(articleId);
        if (article.getId() == 0) {
            throw new ExistsObjectException(NOT_EXISTS_ARTICLE);
        }
        List<CategoryServiceDTO> categories = this.categoryService.findAllByArticleId(article.getId());
        List<PictureServiceDTO> pictures = this.pictureService.findAllByArticleId(article.getId());
        List<VideoServiceDTO> videos = this.videoService.findAllByArticleId(article.getId());
        ArticleServiceDTO viewArticle = new ArticleServiceDTO(article, pictures, categories, videos);
        if (article.getAuthorName() == null) {
            viewArticle.setAuthorName(COPYRIGHT);
        } else {
            viewArticle.setAuthorName(article.getAuthorName());
        }
        this.articleDAO.addViewOfByArticleId(article.getId());
        return viewArticle;
    }

    @Override
    public List<ArticleServiceDTO> findByCategoryId(long categoryId) throws SQLException {
        return ArticleServiceDTO.fromPOJOToDTO(this.articleDAO.articlesByCategoryId(categoryId));
    }

    @Override
    public List<ArticleServiceDTO> findTopFiveReadToday() throws SQLException {
        return ArticleServiceDTO.fromPOJOToDTO(this.articleDAO.topFiveMostViewedArticlesForToday());
    }

    @Override
    public long edit(ArticleServiceDTO serviceDTO, long userId) {
        Article existsArticle = this.articleRepository.findById(serviceDTO.getId())
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_ARTICLE));
        this.checkForAuthorCopyright(existsArticle.getAuthorId(), userId);
        Article article = new Article(serviceDTO.getId(), serviceDTO.getTitle(), serviceDTO.getFullText());
        article.setAuthorId(userId);
        article.setViews(existsArticle.getViews());
        return this.articleRepository.save(article).getId();
    }

    @Override
    public ArticleServiceDTO delete(long articleId) {
        Article existsArticle = this.articleRepository.findById(articleId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_ARTICLE));
        this.articleRepository.deleteById(existsArticle.getId());
        return new ArticleServiceDTO(existsArticle);
    }

    @Override
    public void existsById(long articleId) {
        this.articleRepository.findById(articleId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_ARTICLE));
    }

    @Override
    public void findByIdAndCheckForAuthorCopyright(long articleId, long userId) {
        Article existsArticle = this.articleRepository.findById(articleId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_ARTICLE));
        this.checkForAuthorCopyright(existsArticle.getAuthorId(), userId);
    }

    private void checkForAuthorCopyright(long authorId, long userId) {
        if (authorId != userId) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
    }

    @Override
    public void existsVoteForThatArticleFromThisUser(long articleId, long userId) throws BadRequestException {
        if (this.articleDAO.existsVoteForThatArticleFromThisUser(articleId, userId)) {
            throw new BadRequestException(ALREADY_VOTED);
        }
    }
}
