package sportal.model.service;

import sportal.model.service.dto.CommentServiceDTO;

import java.sql.SQLException;
import java.util.List;

public interface ICommentService {

    String WRONG_INFORMATION = "Wrong information about the user!";
    String NOT_EXISTS_OBJECT = "Comment not found!";

    long addComment(CommentServiceDTO serviceDTO);

    long edit(CommentServiceDTO serviceDTO);

    long delete(long commentId, long userId);

    long deleteFromEditor(long commentId);

    List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws SQLException;

    CommentServiceDTO getCommentsById(long commentId) throws SQLException;

    boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException;

    void existsById(long commentId);
}
