package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.model.dto.comment.*;
import sportal.model.service.CommentService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/comments")
    public CommentResponseDTO addCommentToArticle(@RequestBody CommentCreateDTO commentCreateDTO,
                                                  HttpSession session) throws BadRequestException {
        return this.commentService.addComment(commentCreateDTO,session);
    }

    @PutMapping(value = "/comments")
    public CommentRespDTO editComment(@RequestBody CommentEditDTO commentEditDTO,
                                           HttpSession session) throws BadRequestException {
        return this.commentService.edit(commentEditDTO, session);
    }

    @DeleteMapping(value = "/comments/{" + COMMENT_ID + "}")
    public CommentRespDTO removeComment(@PathVariable(name = COMMENT_ID) long commentId,
                                        HttpSession session) throws BadRequestException {
        return this.commentService.deleteFromUser(commentId, session);
    }

    @DeleteMapping(value = "/comments/admin/{" + COMMENT_ID + "}")
    public CommentRespDTO removeCommentFromAdmin(
            @PathVariable(name = COMMENT_ID) long commentId,
            HttpSession session) throws BadRequestException {
        return this.commentService.deleteFromAdmin(commentId, session);
    }

    @GetMapping(value = "/all_comments/{" + ARTICLE_ID + "}")
    public List<CommentResponseDTO> getAllCommentToArticle(
            @PathVariable(ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        return this.commentService.getAllCommentsByArticleId(articleId);
    }

    @GetMapping(value = "/comments/{" + COMMENT_ID + "}")
    public CommentResponseDTO getComment(
            @PathVariable(COMMENT_ID) long commentId) throws SQLException, BadRequestException {
        return this.commentService.getCommentsById(commentId);
    }
}
