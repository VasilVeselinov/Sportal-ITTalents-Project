package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.model.db.pojo.User;
import sportal.model.service.IVoteService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserLikeDislikeController extends AbstractController {

    @Autowired
    private IVoteService voteService;

    @PostMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> likeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                              HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.voteService.likeArticle(articleId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> removeLikeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                    HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.voteService.deleteVoteForArticle(articleId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> likeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                              HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.voteService.likeComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> dislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                 HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.voteService.dislikeComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeLikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                    HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.voteService.deleteLikeForComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeDislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                       HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.voteService.deleteDislikeForComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
