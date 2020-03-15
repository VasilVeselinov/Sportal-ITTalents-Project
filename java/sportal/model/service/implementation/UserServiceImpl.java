package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.NotExistsObjectException;
import sportal.model.db.dao.IDislikeCommentsDAO;
import sportal.model.db.dao.ILikeArticlesDAO;
import sportal.model.db.dao.ILikeCommentsDAO;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.UserRepository;
import sportal.model.service.IArticleService;
import sportal.model.service.ICommentService;
import sportal.model.service.IUserService;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private static final String ACTIVE_REGISTRATION = "This registration is already active!";
    private static final String NOT_ALLOWED_OPERATION = "The operation you want to perform is not allowed!";
    private static final String NOT_EXISTS_USER = "User not found!";
    private static final int NUMBER_OF_USERS_OF_PAGE = 10;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ILikeArticlesDAO likeArticlesDAO;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ILikeCommentsDAO likeCommentsDAO;
    @Autowired
    private IDislikeCommentsDAO dislikeCommentsDAO;

    @Override
    public UserServiceDTO deleteById(long userId, long editorId) {
        User existsUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_USER));
        if (editorId == userId) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
        this.userRepository.deleteById(userId);
        return new UserServiceDTO(existsUser.getId(), existsUser.getUsername(), existsUser.getUserEmail());
    }

    @Override
    public List<UserServiceDTO> findAll(int page) {
        final int theFirstUserOfThePage = NUMBER_OF_USERS_OF_PAGE * (page - 1);
        return UserServiceDTO.fromPOJOToDTO(
                this.userRepository.findAllByPages(NUMBER_OF_USERS_OF_PAGE, theFirstUserOfThePage));
    }

    @Override
    public UserServiceDTO findById(long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_USER));
        return new UserServiceDTO(user.getId(), user.getUsername(), user.getUserEmail());
    }

    @Override
    public void confirmToken(String token) throws BadRequestException {
        User user = this.userRepository.findByToken(token)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_USER));
        if (user.isEnabled()) {
            throw new BadRequestException(ACTIVE_REGISTRATION);
        }
        user.setEnabled(true);
        this.userRepository.save(user);
    }

    @Override
    public void likeArticle(long articleId, long userId) throws SQLException, BadRequestException {
        this.articleService.existsById(articleId);
        this.articleService.isArticleLikedByUser(articleId, userId);
        this.likeArticlesDAO.add(articleId, userId);
    }

    @Override
    public void deleteVoteForArticle(long articleId, long userId) throws BadRequestException {
        if (this.likeArticlesDAO.delete(articleId, userId) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    @Override
    public void likeComment(long commentId, long userId) throws BadRequestException, SQLException {
        this.commentService.existsById(commentId);
        this.commentService.validateVoteOfCommentByUser(commentId, userId);
        this.likeCommentsDAO.add(commentId, userId);
    }

    @Override
    public void dislikeComment(long commentId, long userId) throws BadRequestException, SQLException {
        this.commentService.existsById(commentId);
        this.commentService.validateVoteOfCommentByUser(commentId, userId);
        this.dislikeCommentsDAO.add(commentId, userId);
    }

    @Override
    public void deleteLikeOfComment(long commentId, long userId) throws BadRequestException {
        if (this.likeCommentsDAO.delete(commentId, userId) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    @Override
    public void deleteDislikeOfComment(long commentId, long userId) throws BadRequestException {
        if (this.dislikeCommentsDAO.delete(commentId, userId) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }
}
