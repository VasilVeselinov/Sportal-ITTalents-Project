package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.CommentServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface ICommentService {
    long addComment(CommentServiceDTO serviceDTO, UserServiceDTO userOfSession) throws BadRequestException;

    long edit(CommentServiceDTO serviceDTO, UserServiceDTO userOfSession) throws BadRequestException;

    long deleteFromUser(long commentId, UserServiceDTO userOfSession) throws BadRequestException;

    long deleteFromAdmin(long commentId, UserServiceDTO userOfSession) throws BadRequestException;

    List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws BadRequestException, SQLException;

    CommentServiceDTO getCommentsById(long commentId) throws BadRequestException, SQLException;

    boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException;

    void existsById(long commentId);
}
