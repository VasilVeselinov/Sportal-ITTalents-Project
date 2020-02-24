package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;

public interface IVoteService {
    void likeArticle(long articleId, UserServiceDTO userOfSession) throws BadRequestException, SQLException;

    void deleteVoteForArticle(long articleId, UserServiceDTO userOfSession) throws BadRequestException;

    void likeComment(long commentId, UserServiceDTO userOfSession) throws BadRequestException, SQLException;

    void dislikeComment(long commentId, UserServiceDTO userOfSession) throws BadRequestException, SQLException;

    void deleteLikeForComment(long commentId, UserServiceDTO userOfSession) throws BadRequestException;

    void deleteDislikeForComment(long commentId, UserServiceDTO userOfSession) throws BadRequestException;
}
