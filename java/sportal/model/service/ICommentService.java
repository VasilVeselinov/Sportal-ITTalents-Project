package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.CommentServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.util.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface ICommentService {

    long addComment(CommentServiceDTO serviceDTO);

    long edit(CommentServiceDTO serviceDTO);

    long delete(long commentId, long userId);

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    long deleteFromEditor(long commentId);

    List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws SQLException;

    CommentServiceDTO getCommentsById(long commentId) throws SQLException;

    void validateVoteOfCommentByUser(long commentId, long userId) throws SQLException, BadRequestException;

    void existsById(long commentId);
}
