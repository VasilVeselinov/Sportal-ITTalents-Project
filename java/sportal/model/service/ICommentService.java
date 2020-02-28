package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.CommentServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface ICommentService {

    String WRONG_INFORMATION = "Wrong information about the user!";
    String NOT_EXISTS_OBJECT = "Comment not found!";

    long addComment(CommentServiceDTO serviceDTO, UserServiceDTO userOfSession) throws BadRequestException;

    long edit(CommentServiceDTO serviceDTO, UserServiceDTO userOfSession) throws BadRequestException;

    long delete(long commentId, UserServiceDTO userOfSession) throws BadRequestException;

    long deleteFromEditor(long commentId) throws BadRequestException;

    List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws BadRequestException, SQLException;

    CommentServiceDTO getCommentsById(long commentId) throws BadRequestException, SQLException;

    boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException;

    void existsById(long commentId);
}
