package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.ArticleDAO;
import sportal.model.service.dto.UserServiceDTO;
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

@Service
public class ArticleServiceImpl implements IArticleService {

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
        List<CategoryServiceDTO> dtoListOfCategories =
                this.categoryService.findAllExistsCategoriseAndCheckIsValid(serviceDTO.getCategories());
        List<PictureServiceDTO> dtoListOfPictures =
                this.pictureService.findAllByArticleIdIsNullAndCheckIsValid(serviceDTO.getPictures());
        Article article = new Article(serviceDTO.getTitle(), serviceDTO.getFullText());
        article.setAuthorId(user.getId());
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
    public long edit(ArticleServiceDTO serviceDTO, UserServiceDTO user) {
        Article existsArticle = this.articleRepository.findById(serviceDTO.getId())
                .orElseThrow(() -> new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS));
        if (existsArticle.getAuthorId() != user.getId()) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
        Article article = new Article(serviceDTO);
        article.setAuthorId(user.getId());
        article.setViews(existsArticle.getViews());
        return this.articleRepository.save(article).getId();
    }

    @Override
    public ArticleServiceDTO delete(long articleId) {
        Article existsArticle = this.articleRepository.findById(articleId)
                .orElseThrow(() -> new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS));
        this.articleRepository.deleteById(existsArticle.getId());
        return new ArticleServiceDTO(existsArticle);
    }

    @Override
    public boolean existsById(long articleId) {
        if (!this.articleRepository.existsById(articleId)){
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        return true;
    }
}
