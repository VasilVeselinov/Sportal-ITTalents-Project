package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.db.pojo.User;

import java.sql.SQLException;

public interface IVoteService {
    void likeArticle(long articleId, User user) throws BadRequestException, SQLException;

    void deleteVoteForArticle(long articleId, User user) throws BadRequestException;

    void likeComment(long commentId, User user) throws BadRequestException, SQLException;

    void dislikeComment(long commentId, User user) throws BadRequestException, SQLException;

    void deleteLikeForComment(long commentId, User user) throws BadRequestException;

    void deleteDislikeForComment(long commentId, User user) throws BadRequestException;
}
