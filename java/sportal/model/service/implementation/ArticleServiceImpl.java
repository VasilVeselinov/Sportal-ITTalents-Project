package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.ArticleDAO;
import sportal.model.dao.ArticlesCategoriesDAO;
import sportal.model.data_validators.ArticleValidator;
import sportal.model.data_validators.UserValidator;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;
import sportal.model.pojo.Picture;
import sportal.model.pojo.User;
import sportal.model.repository.ArticleRepository;
import sportal.model.service.IArticleService;
import sportal.model.service.ICategoryService;
import sportal.model.service.IPictureService;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.dto.PictureServiceDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.THIS_ARTICLE_IS_NOT_EXISTS;
import static sportal.model.data_validators.AbstractValidator.WRONG_REQUEST;

@Service
public class ArticleServiceImpl implements IArticleService {

    private static final String COPYRIGHT = "Sportal holds the copyright of this article.";

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IPictureService pictureService;
    @Autowired
    private ArticleDAO articlesDAO;
    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public long addArticle(ArticleServiceDTO serviceDTO,
                           User user) throws BadRequestException, SQLException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        ArticleServiceDTO validArticle = ArticleValidator.checkArticleForValidData(serviceDTO);

        List<CategoryServiceDTO> dtoListOfCategories =
                this.categoryService.findAllExistsCategoriseAndCheckIsValid(serviceDTO.getCategories());
        List<PictureServiceDTO> dtoListOfPictures =
                this.pictureService.findAllByArticleIdIsNullAndCheckIsValid(serviceDTO.getPictures());
        Article article = new Article(validArticle.getTitle(), validArticle.getFullText());
        article.setAuthorId(logUser.getId());
        article = this.articlesDAO.addArticle(
                article,
                Picture.fromDTOToPojo(dtoListOfPictures),
                Category.fromDTOToPojo(dtoListOfCategories));
        return article.getId();
    }

    @Override
    public List<ArticleServiceDTO> findByArticleTitleOrCategory(String titleOrCategory) throws SQLException {
        return ArticleServiceDTO.fromPOJOToDTO(this.articlesDAO.allArticlesByTitleOrCategory(titleOrCategory));
    }

    @Override
    public ArticleServiceDTO findArticleById(long articleId) throws BadRequestException, SQLException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        Article article = this.articlesDAO.findById(articleId);
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
        this.articlesDAO.addViewOfByArticleId(article.getId());
        return viewArticle;
    }

    @Override
    public List<ArticleServiceDTO> findByCategoryId(long categoryId) throws BadRequestException, SQLException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return ArticleServiceDTO.fromPOJOToDTO(this.articlesCategoriesDAO.allArticlesByCategoryId(categoryId));
    }

    @Override
    public List<ArticleServiceDTO> findTopFiveReadToday() throws SQLException {
        return ArticleServiceDTO.fromPOJOToDTO(this.articlesDAO.topFiveMostViewedArticlesForToday());
    }

    @Override
    public long edit(ArticleServiceDTO serviceDTO, User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        ArticleServiceDTO validArticle = ArticleValidator.validationBeforeEdit(serviceDTO);
        Optional<Article> existsArticle = this.articleRepository.findById(serviceDTO.getId());
        ArticleValidator.conformityCheck(existsArticle, logUser);
        Article article = new Article(validArticle);
        article.setAuthorId(logUser.getId());
        article.setViews(existsArticle.get().getViews());
        return this.articleRepository.save(article).getId();
    }

    @Override
    public ArticleServiceDTO delete(long articleId, User user) throws BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
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
