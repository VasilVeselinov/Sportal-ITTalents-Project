package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.NotExistsObjectExceptions;
import sportal.exception.SomethingWentWrongException;
import sportal.model.DAO.CommentDAO;
import sportal.model.data_validators.CommentValidator;
import sportal.model.data_validators.SessionManagerValidator;
import sportal.model.dto.comment.CommentCreateDTO;
import sportal.model.dto.comment.CommentEditDTO;
import sportal.model.dto.comment.CommentResponseAfterDeleteDTO;
import sportal.model.dto.comment.CommentResponseDTO;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private CommentValidator validator;

    @PostMapping(value = "/comments")
    public CommentResponseDTO addCommentToArticle(@RequestBody CommentCreateDTO commentCreateDTO,
                                                  HttpSession session) throws SQLException, BadRequestException {
        User user = SessionManagerValidator.checkUserIsLogged(session);
        CommentCreateDTO validComment = this.validator.checkForValidDataOfCommentCreateDTO(commentCreateDTO);
        Comment comment = new Comment(validComment, user.getId(), validComment.getArticleId());
        comment = this.commentDAO.addCommentToArticle(comment);
        comment.setUserName(user.getUserName());
        return new CommentResponseDTO(comment);
    }

    @GetMapping(value = "/comments/{" + ARTICLE_ID + "}")
    public List<CommentResponseDTO> addCommentToArticle(
            @PathVariable(ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Comment> comments = this.commentDAO.allCommentsByArticleId(articleId);
        return CommentResponseDTO.fromCommentToCommentResponseDTO(comments);
    }

    @PutMapping(value = "/comments")
    public CommentResponseDTO editComment(@RequestBody CommentEditDTO commentEditDTO,
                                          HttpSession session) throws SQLException, BadRequestException {
        User user = SessionManagerValidator.checkUserIsLogged(session);
        CommentEditDTO validComment = this.validator.checkForValidDataOfCommentEditDTO(commentEditDTO);
        Comment comment = new Comment(validComment);
        Comment existsComment = this.commentDAO.findCommentById(comment.getId());
        if (existsComment == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (user.getId() != existsComment.getUserId()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        if (this.commentDAO.editComment(comment) > 0) {
            comment.setDatePublished(existsComment.getDatePublished());
            comment.setUserName(user.getUserName());
            return new CommentResponseDTO(comment);
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @DeleteMapping(value = "/comments/{" + COMMENT_ID + "}")
    public CommentResponseAfterDeleteDTO deleteComment(@PathVariable(name = COMMENT_ID) long commentId,
                                                       HttpSession session) throws SQLException, BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        Comment existsComment = this.commentDAO.findCommentById(commentId);
        if (existsComment == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        if (existsComment.getUserId() != user.getId()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        this.commentDAO.deleteById(commentId);
        return new CommentResponseAfterDeleteDTO(existsComment);
    }

    @DeleteMapping(value = "/comments/admin/{" + COMMENT_ID + "}")
    public CommentResponseAfterDeleteDTO deleteCommentFromAdmin(
            @PathVariable(name = COMMENT_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        SessionManagerValidator.checkUserIsAdmin(user);
        Comment existsComment = this.commentDAO.findCommentById(commentId);
        if (existsComment == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        this.commentDAO.deleteById(commentId);
        return new CommentResponseAfterDeleteDTO(existsComment);
    }
}
