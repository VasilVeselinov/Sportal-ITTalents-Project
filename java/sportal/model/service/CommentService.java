package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.CommentDAO;
import sportal.model.data_validators.CommentValidator;
import sportal.model.data_validators.SessionValidator;
import sportal.model.dto.comment.*;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;
import sportal.model.repository.ArticleRepository;
import sportal.model.repository.CommentRepository;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.*;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentDAO commentDAO;

    public CommentResponseDTO addComment(CommentCreateDTO commentCreateDTO,
                                         HttpSession session) throws BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        CommentCreateDTO validComment = CommentValidator.checkForValidDataOfCommentCreateDTO(commentCreateDTO);
        if (!this.articleRepository.existsById(validComment.getArticleId())) {
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        Comment comment = new Comment(validComment, user.getId(), validComment.getArticleId());
        comment = this.commentRepository.save(comment);
        comment.setUserName(user.getUserName());
        return new CommentResponseDTO(comment);
    }

    public CommentRespDTO edit(CommentEditDTO commentEditDTO, HttpSession session) throws BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        CommentEditDTO validCommentDTO = CommentValidator.checkForValidDataOfCommentEditDTO(commentEditDTO);
        Optional<Comment> existsComment = this.commentRepository.findById(validCommentDTO.getOldCommentId());
        Comment validExistsComment = CommentValidator.validationOfExistsComment(existsComment, user);
        validExistsComment.setFullCommentText(validCommentDTO.getNewTextOfComment());
        Comment editComment = this.commentRepository.save(validExistsComment);
        return new CommentRespDTO(editComment);
    }

    public CommentRespDTO deleteFromUser(long commentId,
                                         HttpSession session) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        Optional<Comment> existsComment = this.commentRepository.findById(commentId);
        Comment validExistsComment = CommentValidator.validationOfExistsComment(existsComment, user);
        this.commentRepository.deleteById(commentId);
        return new CommentRespDTO(validExistsComment);
    }

    public CommentRespDTO deleteFromAdmin(long commentId, HttpSession session) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Optional<Comment> existsComment = this.commentRepository.findById(commentId);
        if (!existsComment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        this.commentRepository.deleteById(existsComment.get().getId());
        return new CommentRespDTO(existsComment.get());
    }

    public List<CommentResponseDTO> getAllCommentsByArticleId(long articleId) throws BadRequestException, SQLException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Comment> comments = this.commentDAO.allCommentsByArticleId(articleId);
        return CommentResponseDTO.fromCommentToCommentResponseDTO(comments);
    }

    public CommentResponseDTO getCommentsById(long commentId) throws BadRequestException, SQLException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        Comment existsComment = this.commentDAO.findCommentById(commentId);
        if (existsComment == null){
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        return new CommentResponseDTO(existsComment);
    }
}
