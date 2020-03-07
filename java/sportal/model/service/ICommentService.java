package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import sportal.model.service.dto.CommentServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface ICommentService {

    String WRONG_INFORMATION = "Wrong information about the user!";
    String NOT_EXISTS_OBJECT = "Comment not found!";

    long addComment(CommentServiceDTO serviceDTO);

    long edit(CommentServiceDTO serviceDTO);

    long delete(long commentId, long userId);

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    long deleteFromEditor(long commentId);

    List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws SQLException;

    CommentServiceDTO getCommentsById(long commentId) throws SQLException;

    boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException;

    void existsById(long commentId);
}
