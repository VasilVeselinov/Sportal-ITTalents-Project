package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.RoleServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {

    String NOT_ALLOWED_OPERATION = "The operation you want to perform is not allowed!";
    String NOT_EXISTS_OBJECT = "User not found!";

    UserServiceDTO removeUserByUserId(long userId, UserServiceDTO userOfSession) throws BadRequestException;

    List<UserServiceDTO> findAll();

    void likeArticle(long articleId, long userId) throws BadRequestException, SQLException;

    void deleteVoteForArticle(long articleId, long userId) throws BadRequestException;

    void likeComment(long commentId, long userId) throws BadRequestException, SQLException;

    void dislikeComment(long commentId, long userId) throws BadRequestException, SQLException;

    void deleteLikeForComment(long commentId, long userId) throws BadRequestException;

    void deleteDislikeForComment(long commentId, long userId) throws BadRequestException;

    UserServiceDTO findById(long userId);

    void upAuthority(long userId, List<RoleServiceDTO> editorAuthorities) throws BadRequestException;
}
