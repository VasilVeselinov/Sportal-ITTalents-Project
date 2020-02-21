package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.controller.models.comment.CommentResponseModel;
import sportal.model.db.pojo.Comment;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.CommentServiceDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ICommentService {
    long addComment(CommentServiceDTO serviceDTO, User user) throws BadRequestException;

    long edit(CommentServiceDTO serviceDTO, User user) throws BadRequestException;

    long deleteFromUser(long commentId, User user) throws BadRequestException;

    long deleteFromAdmin(long commentId, User user) throws BadRequestException;

    List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws BadRequestException, SQLException;

    CommentServiceDTO getCommentsById(long commentId) throws BadRequestException, SQLException;

    boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException;

    Optional<Comment> findById(long commentId);
}
