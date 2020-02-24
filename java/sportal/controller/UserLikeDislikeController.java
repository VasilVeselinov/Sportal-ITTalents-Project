package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.user.UserResponseModel;
import sportal.exception.*;
import sportal.model.service.IVoteService;
import sportal.model.service.dto.UserServiceDTO;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Positive;
import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserLikeDislikeController extends AbstractController {

    @Autowired
    private IVoteService voteService;

    @PostMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> likeOfArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws SQLException, BadRequestException {
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        this.voteService.likeArticle(articleId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> removeLikeOfArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws BadRequestException {
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        this.voteService.deleteVoteForArticle(articleId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> likeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        this.voteService.likeComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> dislikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        this.voteService.dislikeComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeLikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws BadRequestException {
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        this.voteService.deleteLikeForComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeDislikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws BadRequestException {
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        this.voteService.deleteDislikeForComment(commentId, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
