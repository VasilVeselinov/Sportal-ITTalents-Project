package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.model.db.dao.UsersDislikeCommentsDAO;
import sportal.model.db.dao.UsersLikeArticlesDAO;
import sportal.model.db.dao.UsersLikeCommentsDAO;
import sportal.model.service.dto.UserServiceDTO;
import sportal.model.validators.ArticleValidator;
import sportal.model.validators.UserValidator;
import sportal.model.db.pojo.ExistsObject;
import sportal.model.service.IVoteService;

import java.sql.SQLException;
import java.util.List;

import static sportal.model.validators.AbstractValidator.*;

@Service
public class VoteServiceImpl implements IVoteService {

    private static final String ALREADY_VOTED = "You have already voted on this comment!";

    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private UsersLikeCommentsDAO likeCommentsDAO;
    @Autowired
    private UsersDislikeCommentsDAO dislikeCommentsDAO;

    @Override
    public void likeArticle(long articleId, UserServiceDTO user) throws SQLException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        List<ExistsObject> objectList = this.likeArticlesDAO.existsCombinationAndArticleId(articleId);
        ArticleValidator.validation(objectList, articleId, logUser.getId());
        this.likeArticlesDAO.addInThirdTable(articleId, logUser.getId());
    }

    @Override
    public void deleteVoteForArticle(long articleId, UserServiceDTO user) throws BadRequestException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        if (this.likeArticlesDAO.delete(articleId, logUser.getId()) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    @Override
    public void likeComment(long commentId, UserServiceDTO user) throws BadRequestException, SQLException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        this.commentService.existsById(commentId);
        if (this.commentService.existsVoteForThatCommentFromThisUser(commentId, logUser.getId())) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.likeCommentsDAO.addInThirdTable(commentId, logUser.getId());
    }

    @Override
    public void dislikeComment(long commentId, UserServiceDTO user) throws BadRequestException, SQLException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        this.commentService.existsById(commentId);
        if (this.commentService.existsVoteForThatCommentFromThisUser(commentId, logUser.getId())) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.dislikeCommentsDAO.addInThirdTable(commentId, logUser.getId());
    }

    @Override
    public void deleteLikeForComment(long commentId, UserServiceDTO user) throws BadRequestException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        if (this.likeCommentsDAO.delete(commentId, logUser.getId()) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    @Override
    public void deleteDislikeForComment(long commentId, UserServiceDTO user) throws BadRequestException {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        if (this.dislikeCommentsDAO.delete(commentId, logUser.getId()) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }
}
