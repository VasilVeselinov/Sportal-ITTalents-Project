package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.comment.CommentCreateModel;
import sportal.controller.models.comment.CommentEditModel;
import sportal.controller.models.comment.CommentResponseModel;
import sportal.controller.models.user.UserLoginModel;
import sportal.model.service.ICommentService;
import sportal.model.service.dto.CommentServiceDTO;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.sql.SQLException;
import java.util.List;

import static sportal.util.GlobalConstants.HAS_AUTHORITY_EDITOR;

@RestController
@RequestMapping("/comments")
public class CommentController extends AbstractController {

    @Autowired
    private ICommentService commentService;

    @PostMapping(value = "/add")
    public ResponseEntity<Void> addCommentToArticle(@Valid @RequestBody CommentCreateModel createModel,
                                                    BindingResult bindingResult, HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CommentServiceDTO serviceDTO =
                new CommentServiceDTO(createModel.getCommentText(), createModel.getArticleId());
        serviceDTO.setUserId(logUser.getId());
        long articleId = this.commentService.addComment(serviceDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/all/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<Void> editComment(@Valid @RequestBody CommentEditModel editModel,
                                            BindingResult bindingResult, HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CommentServiceDTO serviceDTO =
                new CommentServiceDTO(editModel.getOldCommentId(), editModel.getNewTextOfComment());
        serviceDTO.setUserId(logUser.getId());
        long commentId = this.commentService.edit(serviceDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/remove/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        long articleId = this.commentService.delete(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/all/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/remove/{" + COMMENT_ID + "}/editor")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public ResponseEntity<Void> removeCommentFromEditor(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId) {
        long articleId = this.commentService.deleteFromEditor(commentId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/all/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/all/{" + ARTICLE_ID + "}")
    public List<CommentResponseModel> allCommentsToArticle(
            @PathVariable(ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId) throws SQLException {
        return CommentResponseModel.fromDTOToModel(this.commentService.getAllCommentsByArticleId(articleId));
    }

    @GetMapping(value = "/{" + COMMENT_ID + "}")
    public CommentResponseModel getComment(
            @PathVariable(COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId) throws SQLException {
        return new CommentResponseModel(this.commentService.getCommentsById(commentId));
    }
}
