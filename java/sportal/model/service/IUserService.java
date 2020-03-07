package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface IUserService {

    String NOT_ALLOWED_OPERATION = "The operation you want to perform is not allowed!";
    String NOT_EXISTS_USER = "User not found!";

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    UserServiceDTO removeUserByUserId(long userId, long editorId);

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    List<UserServiceDTO> findAll();

    void likeArticle(long articleId, long userId) throws SQLException;

    void deleteVoteForArticle(long articleId, long userId) throws BadRequestException;

    void likeComment(long commentId, long userId) throws BadRequestException, SQLException;

    void dislikeComment(long commentId, long userId) throws BadRequestException, SQLException;

    void deleteLikeForComment(long commentId, long userId) throws BadRequestException;

    void deleteDislikeForComment(long commentId, long userId) throws BadRequestException;

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    UserServiceDTO findById(long userId);

    void confirmToken(String token) throws BadRequestException;
}
