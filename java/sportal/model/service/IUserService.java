package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.util.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface IUserService {

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    UserServiceDTO deleteById(long userId, long editorId);

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    List<UserServiceDTO> findAll(int page);

    void likeArticle(long articleId, long userId) throws SQLException, BadRequestException;

    void deleteVoteForArticle(long articleId, long userId) throws BadRequestException;

    void likeComment(long commentId, long userId) throws BadRequestException, SQLException;

    void dislikeComment(long commentId, long userId) throws BadRequestException, SQLException;

    void deleteLikeOfComment(long commentId, long userId) throws BadRequestException;

    void deleteDislikeOfComment(long commentId, long userId) throws BadRequestException;

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    UserServiceDTO findById(long userId);

    void confirmToken(String token) throws BadRequestException;
}
