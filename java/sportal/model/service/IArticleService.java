package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface IArticleService {

    long addArticle(ArticleServiceDTO serviceDTO, UserServiceDTO userOfSession) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findByArticleTitleOrCategory(String titleOrCategory) throws SQLException;

    ArticleServiceDTO findArticleById(long articleId) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findByCategoryId(long categoryId) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findTopFiveReadToday() throws SQLException;

    long edit(ArticleServiceDTO serviceDTO, UserServiceDTO userOfSession) throws BadRequestException;

    ArticleServiceDTO delete(long articleId, UserServiceDTO userOfSession) throws BadRequestException;

    boolean existsById(long articleId);
}
