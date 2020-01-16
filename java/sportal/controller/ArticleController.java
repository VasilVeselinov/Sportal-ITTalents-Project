package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.*;
import sportal.model.data_validators.ArticleValidator;
import sportal.model.data_validators.CategoryValidator;
import sportal.model.data_validators.PictureValidator;
import sportal.model.data_validators.SessionValidator;
import sportal.model.dto.article.*;
import sportal.model.dto.category.CategoryResponseDTO;
import sportal.model.dto.picture.PictureDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.pojo.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticleController extends AbstractController {

    @Autowired
    private ArticleDAO articlesDAO;
    @Autowired
    private PictureDAO pictureDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private ArticlesCategoriesDAO articlesCategoriesDAO;

    @PostMapping(value = "/articles")
    public ArticleAfterCreateDTO addArticle(@RequestBody ArticleCreateDTO articleCreateDTO,
                                            HttpSession session) throws SQLException, BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        ArticleCreateDTO validArticle = ArticleValidator.checkArticleForValidData(articleCreateDTO);
        List<Category> existsCategory = this.categoryDAO.allCategories();
        List<Category> validCategory = CategoryValidator.conformityCheck(existsCategory, articleCreateDTO.getCategories());
        List<Picture> existsPictures = this.pictureDAO.AllPicturesWhereArticleIdIsNull();
        List<Picture> validPictures = PictureValidator.conformityCheck(existsPictures, articleCreateDTO.getPictures());
        Article article = new Article(validArticle);
        article.setAuthorId(user.getId());
        article = this.articlesDAO.addArticle(article, validPictures, validCategory);
        List<Picture> listFromPicturesAfterSetArticleId = this.pictureDAO.allPicturesByArticleId(article.getId());
        List<PictureDTO> picturesDTO = PictureDTO.fromPictureToPictureDTO(listFromPicturesAfterSetArticleId);
        List<Category> categories = this.articlesCategoriesDAO.allCategoriesByArticlesId(article.getId());
        List<CategoryResponseDTO> categoriesDTO = CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(categories);
        UserResponseDTO userResponseDTO = new UserResponseDTO(user);
        return new ArticleAfterCreateDTO(article, categoriesDTO, picturesDTO, userResponseDTO);
    }

    @GetMapping(value = "/articles/search")
    public List<ArticleRespDTO> searchOfArticlesByTitleOfCategoryName(
            @RequestParam(name = "text", required = false) String titleOrCategory) throws SQLException {
        List<Article> listFromArticles = this.articlesDAO.allArticlesByTitleOrCategory(titleOrCategory);
        List<ArticleRespDTO> listFromReturnArticle = new ArrayList<>();
        for (Article article : listFromArticles) {
            listFromReturnArticle.add(new ArticleRespDTO(article));
        }
        return listFromReturnArticle;
    }

    @GetMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public ArticleFullDataDTO articleById(
            @PathVariable(name = ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        Article article = this.articlesDAO.articleById(articleId);
        if (article.getId() == 0) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        ArticleFullDataDTO viewArticle = new ArticleFullDataDTO(article);
        List<Category> categories = this.articlesCategoriesDAO.allCategoriesByArticlesId(articleId);
        viewArticle.setCategories(CategoryResponseDTO.fromCategoryListToCategoryResponseDTO(categories));
        List<Picture> pictures = this.pictureDAO.allPicturesByArticleId(articleId);
        viewArticle.setPictures(PictureDTO.fromPictureToPictureDTO(pictures));
        if (article.getAuthorName() == null) {
            viewArticle.setAuthorName(COPYRIGHT);
        } else {
            viewArticle.setAuthorName(article.getAuthorName());
        }
        this.articlesDAO.addViewOfByArticleId(articleId);
        return viewArticle;
    }

    @GetMapping(value = "/articles/the_category/{" + CATEGORY_ID + "}")
    public List<ArticleRespDTO> articlesByCategoryId(
            @PathVariable(CATEGORY_ID) long categoryId) throws SQLException, BadRequestException {
        if (categoryId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Article> articles = this.articlesCategoriesDAO.allArticlesByCategoryId(categoryId);
        return ArticleRespDTO.fromArticleToArticleRespDTO(articles);
    }

    @GetMapping(value = "/articles/top_5_view_articles")
    public List<ArticleRespDTO> topFiveViewedArticlesToday() throws SQLException {
        List<Article> listFromArticles = this.articlesDAO.topFiveMostViewedArticlesForToday();
        List<ArticleRespDTO> listFromReturnArticle = new ArrayList<>();
        for (Article article : listFromArticles) {
            listFromReturnArticle.add(new ArticleRespDTO(article));
        }
        return listFromReturnArticle;
    }


    @PutMapping(value = "/articles")
    public ArticleAfterEditDTO editArticleTitleOrText(@RequestBody ArticleEditDTO articleEditDTO,
                                                      HttpSession session) throws SQLException, BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        ArticleEditDTO validArticle = ArticleValidator.validationBeforeEdit(articleEditDTO);
        Article article = new Article(validArticle);
        Article editedArticle = this.articlesDAO.editOfTitleAndFullText(article);
        if (editedArticle != null) {
            return new ArticleAfterEditDTO(editedArticle);
        } else {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
    }

    @DeleteMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public ArticleRespDTO deleteArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                        HttpSession session) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Article article = this.articlesDAO.articleById(articleId);
        if (article == null) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        this.articlesDAO.deleteById(articleId);
        return new ArticleRespDTO(article);
    }
}
