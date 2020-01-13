package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.exception.SomethingWentWrongException;
import sportal.model.dao.CommentDAO;
import sportal.model.data_validators.CommentValidator;
import sportal.model.data_validators.SessionValidator;
import sportal.model.dto.comment.*;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentDAO commentDAO;

    @PostMapping(value = "/comments")
    public CommentResponseDTO addCommentToArticle(@RequestBody CommentCreateDTO commentCreateDTO,
                                                  HttpSession session) throws SQLException, BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        CommentCreateDTO validComment = CommentValidator.checkForValidDataOfCommentCreateDTO(commentCreateDTO);
        Comment comment = new Comment(validComment, user.getId(), validComment.getArticleId());
        comment = this.commentDAO.addCommentToArticle(comment);
        comment.setUserName(user.getUserName());
        return new CommentResponseDTO(comment);
    }

    @GetMapping(value = "/comments/{" + ARTICLE_ID + "}")
    public List<CommentResponseDTO> getAllCommentToArticle(
            @PathVariable(ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Comment> comments = this.commentDAO.allCommentsByArticleId(articleId);
        return CommentResponseDTO.fromCommentToCommentResponseDTO(comments);
    }

    @PutMapping(value = "/comments")
    public CommentAfterEditDTO editComment(@RequestBody CommentEditDTO commentEditDTO,
                                          HttpSession session) throws SQLException, BadRequestException {
        User user = SessionValidator.checkUserIsLogged(session);
        CommentEditDTO validComment = CommentValidator.checkForValidDataOfCommentEditDTO(commentEditDTO);
        Comment comment = new Comment(validComment);
        Comment existsComment = this.commentDAO.findCommentById(comment.getId());
        Comment validExistsComment = CommentValidator.validationOfExistsComment(existsComment, user);
        Comment editComment = this.commentDAO.editComment(validExistsComment);
        if (editComment != null) {
            return new CommentAfterEditDTO(editComment);
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
        User user = SessionValidator.checkUserIsLogged(session);
        Comment existsComment = this.commentDAO.findCommentById(commentId);
        Comment validExistsComment = CommentValidator.validationOfExistsComment(existsComment, user);
        this.commentDAO.deleteById(commentId);
        return new CommentResponseAfterDeleteDTO(validExistsComment);
    }

    @DeleteMapping(value = "/comments/admin/{" + COMMENT_ID + "}")
    public CommentResponseAfterDeleteDTO deleteCommentFromAdmin(
            @PathVariable(name = COMMENT_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Comment existsComment = this.commentDAO.findCommentById(commentId);
        if (existsComment == null) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        this.commentDAO.deleteById(commentId);
        return new CommentResponseAfterDeleteDTO(existsComment);
    }
}
