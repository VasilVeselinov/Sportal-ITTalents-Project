package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sportal.exception.*;
import sportal.model.DAO.UsersDislikeCommentsDAO;
import sportal.model.DAO.UsersLikeArticlesDAO;
import sportal.model.DAO.UsersLikeCommentsDAO;
import sportal.model.data_validators.SessionManagerValidator;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class UserLikeDislikeController extends AbstractController {

    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;
    @Autowired
    private UsersLikeCommentsDAO likeCommentsDAO;
    @Autowired
    private UsersDislikeCommentsDAO dislikeCommentsDAO;

    @PostMapping(value = "/users/like_articles/{" + ARTICLE_ID + "}")
    public long likeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                              HttpSession session) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        if (this.likeArticlesDAO.existsInThirdTable(articleId, user.getId())) {
            throw new ExistsObjectException(WITHOUT_MORE_VOTE);
        }
        if (this.likeArticlesDAO.addInThirdTable(articleId, user.getId()) > 0) {
            return articleId;
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @DeleteMapping(value = "/users/like_articles/{" + ARTICLE_ID + "}")
    public long deleteLikeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                    HttpSession session) throws SQLException, BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        if (this.likeArticlesDAO.deleteFromThirdTable(articleId, user.getId()) > 0) {
            return articleId;
        } else {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }

    @PostMapping(value = "/users/like_comments/{" + COMMENT_ID + "}")
    public long likeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                              HttpSession session) throws SQLException, BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        if (this.likeCommentsDAO.existsInThirdTable(commentId, user.getId())) {
            throw new ExistsObjectException(ALREADY_VOTED);
        }
        if (this.dislikeCommentsDAO.existsInThirdTable(commentId, user.getId())) {
            throw new ExistsObjectException(ALREADY_VOTED);
        }
        if (this.likeCommentsDAO.addInThirdTable(commentId, user.getId()) > 0) {
            return commentId;
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @PostMapping(value = "/users/dislike_comments/{" + COMMENT_ID + "}")
    public long dislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                 HttpSession session) throws SQLException, BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        if (this.dislikeCommentsDAO.existsInThirdTable(commentId, user.getId())) {
            throw new ExistsObjectException(ALREADY_VOTED);
        }
        if (this.likeCommentsDAO.existsInThirdTable(commentId, user.getId())) {
            throw new ExistsObjectException(ALREADY_VOTED);
        }
        if (this.dislikeCommentsDAO.addInThirdTable(commentId, user.getId()) > 0) {
            return commentId;
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @DeleteMapping(value = "/users/like_comments/{" + COMMENT_ID + "}")
    public long deleteLikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                    HttpSession session) throws SQLException, BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        if (this.likeCommentsDAO.deleteFromThirdTable(commentId, user.getId()) > 0) {
            return commentId;
        } else {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }

    @DeleteMapping(value = "/users/dislike_comments/{" + COMMENT_ID + "}")
    public long deleteDislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                       HttpSession session) throws SQLException, BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        if (this.dislikeCommentsDAO.deleteFromThirdTable(commentId, user.getId()) > 0) {
            return commentId;
        } else {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }
}
