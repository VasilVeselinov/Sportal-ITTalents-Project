package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.UsersDislikeCommentsDAO;
import sportal.model.dao.UsersLikeArticlesDAO;
import sportal.model.dao.UsersLikeCommentsDAO;
import sportal.model.data_validators.ArticleValidator;
import sportal.model.data_validators.UserValidator;
import sportal.model.pojo.Comment;
import sportal.model.pojo.ExistsObject;
import sportal.model.pojo.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.*;

@Service
public class VoteService {

    private static final String ALREADY_VOTED = "You have already voted on this comment!";

    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UsersLikeCommentsDAO likeCommentsDAO;
    @Autowired
    private UsersDislikeCommentsDAO dislikeCommentsDAO;

    public long likeArticle(long articleId, User user) throws BadRequestException, SQLException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        List<ExistsObject> objectList = this.likeArticlesDAO.existsCombinationAndArticleId(articleId);
        ArticleValidator.validation(objectList, articleId, logUser.getId());
        this.likeArticlesDAO.addInThirdTable(articleId, logUser.getId());
        return articleId;
    }

    public long deleteVoteForArticle(long articleId, User user) throws BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        if (this.likeArticlesDAO.delete(articleId, logUser.getId()) > 0) {
            return articleId;
        } else {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    public long likeComment(long commentId, User user) throws BadRequestException, SQLException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        Optional<Comment> comment = this.commentService.findById(commentId);
        if (!comment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (this.commentService.existsVoteForThatCommentFromThisUser(commentId, logUser.getId())) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.likeCommentsDAO.addInThirdTable(commentId, logUser.getId());
        return commentId;
    }

    public long dislikeComment(long commentId, User user) throws BadRequestException, SQLException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        Optional<Comment> comment = this.commentService.findById(commentId);
        if (!comment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (this.commentService.existsVoteForThatCommentFromThisUser(commentId, logUser.getId())) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.dislikeCommentsDAO.addInThirdTable(commentId, logUser.getId());
        return commentId;
    }

    public long deleteLikeForComment(long commentId, User user) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        if (this.likeCommentsDAO.delete(commentId, logUser.getId()) > 0) {
            return commentId;
        } else {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    public long deleteDislikeForComment(long commentId, User user) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        if (this.dislikeCommentsDAO.delete(commentId, logUser.getId()) > 0) {
            return commentId;
        } else {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }
}
