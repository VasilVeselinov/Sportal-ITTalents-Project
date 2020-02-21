package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.comment.CommentCreateModel;
import sportal.controller.models.comment.CommentEditModel;
import sportal.controller.models.comment.CommentResponseModel;
import sportal.exception.BadRequestException;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.CommentServiceDTO;
import sportal.model.service.implementation.CommentServiceImpl;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController extends AbstractController {

    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping(value = "/add")
    public ResponseEntity<Void> addCommentToArticle(@RequestBody CommentCreateModel commentModel,
                                                    HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CommentServiceDTO serviceDTO = new CommentServiceDTO(commentModel);
        long articleId = this.commentService.addComment(serviceDTO, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/all/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<Void> editComment(@RequestBody CommentEditModel commentModel,
                                            HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        CommentServiceDTO serviceDTO = new CommentServiceDTO(commentModel);
        long commentId = this.commentService.edit(serviceDTO, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/remove/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeComment(@PathVariable(name = COMMENT_ID) long commentId,
                                              HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        long articleId = this.commentService.deleteFromUser(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/all/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/remove/admin/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeCommentFromAdmin(@PathVariable(name = COMMENT_ID) long commentId,
                                                       HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        long articleId = this.commentService.deleteFromAdmin(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/all/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/all/{" + ARTICLE_ID + "}")
    public List<CommentResponseModel> getAllCommentToArticle(
            @PathVariable(ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        return CommentResponseModel.fromDTOToModel(this.commentService.getAllCommentsByArticleId(articleId));
    }

    @GetMapping(value = "/{" + COMMENT_ID + "}")
    public CommentResponseModel getComment(
            @PathVariable(COMMENT_ID) long commentId) throws SQLException, BadRequestException {
        return new CommentResponseModel(this.commentService.getCommentsById(commentId));
    }
}
