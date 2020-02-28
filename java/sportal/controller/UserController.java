package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.user.UserLoginModel;
import sportal.controller.models.user.UserResponseModel;
import sportal.controller.util.AuthValidator;
import sportal.exception.*;
import sportal.model.service.IUserService;
import sportal.model.service.dto.UserServiceDTO;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Positive;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    @Autowired
    private IUserService userService;

    @DeleteMapping(value = "/remove/{" + USER_ID + "}")
    public UserResponseModel removeUser(
            @PathVariable(name = USER_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long userId,
            HttpSession session) throws BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsEditor(logUser);
        UserServiceDTO user = new UserServiceDTO(logUser.getId(), logUser.getUsername(), logUser.getUserEmail());
        return new UserResponseModel(this.userService.removeUserByUserId(userId, user));
    }

    @GetMapping(value = "/all")
    public List<UserResponseModel> getAllUsers(HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsEditor(logUser);
        return UserResponseModel.fromDTOToModel(this.userService.findAll());
    }

    @GetMapping(value = "/{" + USER_ID + "}")
    public UserResponseModel getUsersById(
            @PathVariable(name = USER_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long userId,
            HttpSession session) {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsEditor(logUser);
        return new UserResponseModel(this.userService.findById(userId));
    }

    @PutMapping(value = "/up_authority/{" + USER_ID + "}")
    public ResponseEntity<Void> upAuthorityOfUsersById(
            @PathVariable(name = USER_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long userId,
            HttpSession session) throws BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsEditor(logUser);
        this.userService.upAuthority(userId, logUser.getAuthorities());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/users/" + userId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> likeOfArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws SQLException, BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.likeArticle(articleId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public ResponseEntity<Void> removeLikeOfArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.deleteVoteForArticle(articleId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> likeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.likeComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> dislikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws SQLException, BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.dislikeComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeLikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.deleteLikeForComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public ResponseEntity<Void> removeDislikeOfComment(
            @PathVariable(name = COMMENT_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long commentId,
            HttpSession session) throws BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.userService.deleteDislikeForComment(commentId, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/comments/" + commentId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
