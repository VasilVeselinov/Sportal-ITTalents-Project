package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.ICommentDAO;
import sportal.model.service.IArticleService;
import sportal.model.service.dto.CommentServiceDTO;
import sportal.model.db.pojo.Comment;
import sportal.model.db.repository.CommentRepository;
import sportal.model.service.ICommentService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    private static final String ALREADY_VOTED = "You have already voted on this comment!";
    private static final String YOU_ARE_NOT_AUTHOR = "You are not author of this comment!";
    private static final String NOT_EXISTS_COMMENT = "Comment not found!";

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentDAO commentDAO;

    @Override
    public long addComment(CommentServiceDTO serviceDTO) {
        this.articleService.existsById(serviceDTO.getArticleId());
        Comment comment = new Comment(serviceDTO.getText(), serviceDTO.getArticleId() , serviceDTO.getUserId());
        comment = this.commentRepository.save(comment);
        comment.setUserName(serviceDTO.getUserName());
        return comment.getArticleId();
    }

    @Override
    public long edit(CommentServiceDTO serviceDTO) {
        Comment existsComment = this.commentRepository.findById(serviceDTO.getId())
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_COMMENT));
        if (serviceDTO.getUserId() != existsComment.getUserId()) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
        existsComment.setFullCommentText(serviceDTO.getText());
        existsComment.setDatePublished(Timestamp.valueOf(LocalDateTime.now()));
        Comment editComment = this.commentRepository.save(existsComment);
        return editComment.getId();
    }

    @Override
    public long delete(long commentId, long userId) {
        Comment existsComment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_COMMENT));
        if (userId != existsComment.getUserId()) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
        this.commentRepository.deleteById(commentId);
        return existsComment.getArticleId();
    }

    @Override
    public long deleteFromEditor(long commentId) {
        Comment existsComment = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_COMMENT));
        this.commentRepository.deleteById(existsComment.getId());
        return existsComment.getArticleId();
    }

    @Override
    public List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws SQLException {
        List<Comment> comments = this.commentDAO.allCommentsByArticleId(articleId);
        return CommentServiceDTO.fromPOJOToDTO(comments);
    }

    @Override
    public CommentServiceDTO getCommentsById(long commentId) throws SQLException {
        Comment existsComment = this.commentDAO.findById(commentId);
        if (existsComment == null) {
            throw new ExistsObjectException(NOT_EXISTS_COMMENT);
        }
        return new CommentServiceDTO(existsComment);
    }

    @Override
    public void existsVoteForThatCommentFromThisUser(
            long commentId, long userId) throws SQLException, BadRequestException {
        if (this.commentDAO.existsVoteForThatCommentFromThisUser(commentId, userId)){
            throw new BadRequestException(ALREADY_VOTED);
        }
    }

    @Override
    public void existsById(long commentId) {
        if (!this.commentRepository.existsById(commentId)) {
            throw new ExistsObjectException(NOT_EXISTS_COMMENT);
        }
    }
}
