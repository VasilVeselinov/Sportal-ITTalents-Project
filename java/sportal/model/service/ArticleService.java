package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.ArticleDAO;
import sportal.model.dao.ArticlesCategoriesDAO;
import sportal.model.data_validators.ArticleValidator;
import sportal.model.data_validators.CategoryValidator;
import sportal.model.data_validators.PictureValidator;
import sportal.model.data_validators.UserValidator;
import sportal.model.dto.article.*;
import sportal.model.dto.category.CategoryResponseDTO;
import sportal.model.dto.picture.PictureToTheArticleDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.pojo.Article;
import sportal.model.pojo.Category;
import sportal.model.pojo.Picture;
import sportal.model.pojo.User;
import sportal.model.repository.ArticleRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.THIS_ARTICLE_IS_NOT_EXISTS;
import static sportal.model.data_validators.AbstractValidator.WRONG_REQUEST;

@Service
public class ArticleService {

    private static final String COPYRIGHT = "Sportal holds the copyright of this article.";

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private ArticleDAO articlesDAO;
    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;
    @Autowired
    private ArticleRepository articleRepository;

    public ArticleAfterCreateDTO addArticle(ArticleCreateDTO articleCreateDTO,
                                            User user) throws BadRequestException, SQLException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        ArticleCreateDTO validArticle = ArticleValidator.checkArticleForValidData(articleCreateDTO);
        List<Category> existsCategories = this.categoryService.findAllExistsCategorise();
        List<Category> validCategories = CategoryValidator.conformityCheck(existsCategories, articleCreateDTO.getCategories());
        List<Picture> existsPictures = this.pictureService.findAllByArticleIdIsNull();
        List<Picture> validPictures = PictureValidator.conformityCheck(existsPictures, articleCreateDTO.getPictures());
        Article article = new Article(validArticle);
        article.setAuthorId(logUser.getId());
        article = this.articlesDAO.addArticle(article, validPictures, validCategories);
        List<Picture> listFromPicturesAfterSetArticleId = this.pictureService.findAllByArticleId(article.getId());
        List<PictureToTheArticleDTO> picturesDTO =
                PictureToTheArticleDTO.fromPictureToPictureToTheArticleDTO(listFromPicturesAfterSetArticleId);
        List<Category> categories = this.articlesCategoriesDAO.allCategoriesByArticlesId(article.getId());
        List<CategoryResponseDTO> categoriesDTO = CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(categories);
        return new ArticleAfterCreateDTO(article, categoriesDTO, picturesDTO, new UserResponseDTO(logUser));
    }

    public List<ArticleRespDTO> findByArticleTitleOrCategory(String titleOrCategory) throws SQLException {
        List<Article> articles = this.articlesDAO.allArticlesByTitleOrCategory(titleOrCategory);
        return ArticleRespDTO.fromArticleToArticleRespDTO(articles);
    }

    public ArticleFullDataDTO findArticleById(long articleId) throws BadRequestException, SQLException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        Article article = this.articlesDAO.findById(articleId);
        if (article.getId() == 0) {
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        ArticleFullDataDTO viewArticle = new ArticleFullDataDTO(article);
        List<Category> categories = this.articlesCategoriesDAO.allCategoriesByArticlesId(article.getId());
        viewArticle.setCategories(CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(categories));
        List<Picture> pictures = this.pictureService.findAllByArticleId(article.getId());
        viewArticle.setPictures(PictureToTheArticleDTO.fromPictureToPictureToTheArticleDTO(pictures));
        if (article.getAuthorName() == null) {
            viewArticle.setAuthorName(COPYRIGHT);
        } else {
            viewArticle.setAuthorName(article.getAuthorName());
        }
        this.articlesDAO.addViewOfByArticleId(article.getId());
        return viewArticle;
    }

    public List<ArticleRespDTO> findByCategoryId(long categoryId) throws BadRequestException, SQLException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Article> articles = this.articlesCategoriesDAO.allArticlesByCategoryId(categoryId);
        return ArticleRespDTO.fromArticleToArticleRespDTO(articles);
    }

    public List<ArticleRespDTO> findTopFiveReadToday() throws SQLException {
        List<Article> listFromArticles = this.articlesDAO.topFiveMostViewedArticlesForToday();
        List<ArticleRespDTO> listFromReturnArticle = new ArrayList<>();
        for (Article article : listFromArticles) {
            listFromReturnArticle.add(new ArticleRespDTO(article));
        }
        return listFromReturnArticle;
    }

    public ArticleAfterEditDTO edit(ArticleEditDTO articleEditDTO, User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        ArticleEditDTO validArticle = ArticleValidator.validationBeforeEdit(articleEditDTO);
        Optional<Article> existsArticle = this.articleRepository.findById(articleEditDTO.getOldArticleId());
        ArticleValidator.conformityCheck(existsArticle, logUser);
        Article article = new Article(validArticle);
        article.setAuthorId(logUser.getId());
        article.setViews(existsArticle.get().getViews());
        Article editedArticle = this.articleRepository.save(article);
        return new ArticleAfterEditDTO(editedArticle);
    }

    public ArticleRespDTO delete(long articleId, User user) throws BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Article> existsArticle = this.articleRepository.findById(articleId);
        ArticleValidator.conformityCheck(existsArticle, logUser);
        this.articleRepository.delete(existsArticle.get());
        return new ArticleRespDTO(existsArticle.get());
    }

    boolean existsById(long articleId) {
        return this.articleRepository.existsById(articleId);
    }
}
