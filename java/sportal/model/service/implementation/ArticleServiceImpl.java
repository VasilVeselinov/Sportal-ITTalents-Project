package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.ArticleDAO;
import sportal.model.service.dto.UserServiceDTO;
import sportal.model.validators.ArticleValidator;
import sportal.model.validators.UserValidator;
import sportal.model.db.pojo.Article;
import sportal.model.db.pojo.Category;
import sportal.model.db.pojo.Picture;
import sportal.model.db.repository.ArticleRepository;
import sportal.model.service.IArticleService;
import sportal.model.service.ICategoryService;
import sportal.model.service.IPictureService;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.dto.PictureServiceDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static sportal.model.validators.AbstractValidator.THIS_ARTICLE_IS_NOT_EXISTS;

@Service
public class ArticleServiceImpl implements IArticleService {

    private static final String COPYRIGHT = "Sportal holds the copyright of this article.";

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IPictureService pictureService;
    @Autowired
    private ArticleDAO articleDAO;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public long addArticle(ArticleServiceDTO serviceDTO, UserServiceDTO user) throws SQLException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        List<CategoryServiceDTO> dtoListOfCategories =
                this.categoryService.findAllExistsCategoriseAndCheckIsValid(serviceDTO.getCategories());
        List<PictureServiceDTO> dtoListOfPictures =
                this.pictureService.findAllByArticleIdIsNullAndCheckIsValid(serviceDTO.getPictures());
        Article article = new Article(serviceDTO.getTitle(), serviceDTO.getFullText());
        article.setAuthorId(logUser.getId());
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
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        List<CategoryServiceDTO> categories = this.categoryService.findAllByArticleId(article.getId());
        List<PictureServiceDTO> pictures = this.pictureService.findAllByArticleId(article.getId());
        ArticleServiceDTO viewArticle = new ArticleServiceDTO(article, pictures, categories);
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
    public long edit(ArticleServiceDTO serviceDTO, UserServiceDTO user) throws BadRequestException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Article> existsArticle = this.articleRepository.findById(serviceDTO.getId());
        ArticleValidator.conformityCheck(existsArticle, logUser);
        Article article = new Article(serviceDTO);
        article.setAuthorId(logUser.getId());
        article.setViews(existsArticle.get().getViews());
        return this.articleRepository.save(article).getId();
    }

    @Override
    public ArticleServiceDTO delete(long articleId, UserServiceDTO user) throws BadRequestException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Article> existsArticle = this.articleRepository.findById(articleId);
        ArticleValidator.conformityCheck(existsArticle, logUser);
        this.articleRepository.deleteById(existsArticle.get().getId());
        return new ArticleServiceDTO(existsArticle.get());
    }

    @Override
    public boolean existsById(long articleId) {
        return this.articleRepository.existsById(articleId);
    }
}
