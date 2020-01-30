package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dao.CommentDAO;
import sportal.model.dao.UsersDislikeCommentsDAO;
import sportal.model.dao.UsersLikeArticlesDAO;
import sportal.model.dao.UsersLikeCommentsDAO;
import sportal.model.data_validators.ArticleValidator;
import sportal.model.data_validators.SessionValidator;
import sportal.model.pojo.Comment;
import sportal.model.pojo.ExistsObject;
import sportal.model.pojo.User;
import sportal.model.repository.CommentRepository;

import javax.servlet.http.HttpSession;

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
    private CommentRepository commentRepository;
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private UsersLikeCommentsDAO likeCommentsDAO;
    @Autowired
    private UsersDislikeCommentsDAO dislikeCommentsDAO;

    public long likeArticle(long articleId, HttpSession session) throws BadRequestException, SQLException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        List<ExistsObject> objectList = this.likeArticlesDAO.existsCombinationAndArticleId(articleId);
        ArticleValidator.validation(objectList, articleId, user.getId());
        this.likeArticlesDAO.addInThirdTable(articleId, user.getId());
        return articleId;
    }

    public long deleteVoteForArticle(long articleId, HttpSession session) throws BadRequestException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        if (this.likeArticlesDAO.delete(articleId, user.getId()) > 0) {
            return articleId;
        } else {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    public long likeComment(long commentId, HttpSession session) throws BadRequestException, SQLException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        Optional<Comment> comment = this.commentRepository.findById(commentId);
        if (!comment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (this.commentDAO.existsVoteForThatCommentFromThisUser(commentId, user.getId())) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.likeCommentsDAO.addInThirdTable(commentId, user.getId());
        return commentId;
    }

    public long dislikeComment(long commentId, HttpSession session) throws BadRequestException, SQLException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        Optional<Comment> comment = this.commentRepository.findById(commentId);
        if (!comment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (this.commentDAO.existsVoteForThatCommentFromThisUser(commentId, user.getId())) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.dislikeCommentsDAO.addInThirdTable(commentId, user.getId());
        return commentId;
    }

    public long deleteLikeForComment(long commentId, HttpSession session) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        if (this.likeCommentsDAO.delete(commentId, user.getId()) > 0) {
            return commentId;
        } else {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    public long deleteDislikeForComment(long commentId, HttpSession session) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        if (this.dislikeCommentsDAO.delete(commentId, user.getId()) > 0) {
            return commentId;
        } else {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }
}
