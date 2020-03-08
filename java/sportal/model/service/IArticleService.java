package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.ArticleServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_ADMIN;
import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface IArticleService {

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    long addArticle(ArticleServiceDTO serviceDTO, long userId) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findByArticleTitleOrCategory(String titleOrCategory) throws SQLException;

    ArticleServiceDTO findArticleById(long articleId) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findByCategoryId(long categoryId) throws BadRequestException, SQLException;

    List<ArticleServiceDTO> findTopFiveReadToday() throws SQLException;

    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    long edit(ArticleServiceDTO serviceDTO, long userId) throws BadRequestException;

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    ArticleServiceDTO delete(long articleId) throws BadRequestException;

    void existsById(long articleId);

    void existsVoteForThatArticleFromThisUser(long articleId, long userId) throws BadRequestException;
}
