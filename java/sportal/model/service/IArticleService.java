package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.pojo.User;
import sportal.model.service.dto.ArticleServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface IArticleService {

    long addArticle(ArticleServiceDTO serviceDTO, User user) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findByArticleTitleOrCategory(String titleOrCategory) throws SQLException;

    ArticleServiceDTO findArticleById(long articleId) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findByCategoryId(long categoryId) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findTopFiveReadToday() throws SQLException;

    long edit(ArticleServiceDTO serviceDTO, User user) throws BadRequestException;

    ArticleServiceDTO delete(long articleId, User user) throws BadRequestException;

    boolean existsById(long articleId);
}
