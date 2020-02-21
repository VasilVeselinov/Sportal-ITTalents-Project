package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.pojo.User;

import java.sql.SQLException;

public interface IVoteService {
    long likeArticle(long articleId, User user) throws BadRequestException, SQLException;

    long deleteVoteForArticle(long articleId, User user) throws BadRequestException;

    long likeComment(long commentId, User user) throws BadRequestException, SQLException;

    long dislikeComment(long commentId, User user) throws BadRequestException, SQLException;

    long deleteLikeForComment(long commentId, User user) throws BadRequestException;

    long deleteDislikeForComment(long commentId, User user) throws BadRequestException;
}
