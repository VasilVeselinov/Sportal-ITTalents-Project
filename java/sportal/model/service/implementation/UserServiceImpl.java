package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.UserDAO;
import sportal.model.db.dao.UsersDislikeCommentsDAO;
import sportal.model.db.dao.UsersLikeArticlesDAO;
import sportal.model.db.dao.UsersLikeCommentsDAO;
import sportal.model.db.pojo.ExistsObject;
import sportal.model.db.pojo.Role;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.UserRepository;
import sportal.model.service.IRoleService;
import sportal.model.service.IUserService;
import sportal.model.service.dto.RoleServiceDTO;
import sportal.model.service.dto.UserServiceDTO;
import sportal.model.validators.ArticleValidator;

import java.sql.SQLException;
import java.util.List;

import static sportal.model.service.implementation.AuthServiceImpl.ADMIN_USER_AUTHORITY;

@Service
public class UserServiceImpl implements IUserService {

    private static final String ALREADY_VOTED = "You have already voted on this comment!";
    private static final String NO_MORE_ACCESS_RIGHTS = "No more access rights!";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersLikeArticlesDAO likeArticlesDAO;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private UsersLikeCommentsDAO likeCommentsDAO;
    @Autowired
    private UsersDislikeCommentsDAO dislikeCommentsDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private IRoleService roleService;

    @Override
    public UserServiceDTO removeUserByUserId(long userId, UserServiceDTO user) {
        User existsUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));
        if (user.getId() == userId) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
        this.userRepository.deleteById(userId);
        return new UserServiceDTO(existsUser);
    }

    @Override
    public List<UserServiceDTO> findAll() {
        return UserServiceDTO.fromPOJOToDTO(this.userRepository.findAll());
    }

    @Override
    public UserServiceDTO findById(long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));
        return new UserServiceDTO(user);
    }

    @Override
    public void upAuthority(long userId, List<RoleServiceDTO> editorAuthorities) throws BadRequestException {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));
        List<Role> roles = this.userDAO.findAllRolesByUserId(userId);
        if (roles.size() >= editorAuthorities.size() - 1) {
            throw new BadRequestException(NO_MORE_ACCESS_RIGHTS);
        }
        this.userDAO.upAuthorityByUserId(userId, this.roleService.getAuthorities(ADMIN_USER_AUTHORITY).getId());
    }

    @Override
    public void likeArticle(long articleId, long userId) throws SQLException {
        List<ExistsObject> objectList = this.likeArticlesDAO.existsCombinationAndArticleId(articleId);
        ArticleValidator.validation(objectList, articleId, userId);
        this.likeArticlesDAO.addInThirdTable(articleId, userId);
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
        if (this.commentService.existsVoteForThatCommentFromThisUser(commentId, userId)) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.likeCommentsDAO.addInThirdTable(commentId, userId);
    }

    @Override
    public void dislikeComment(long commentId, long userId) throws BadRequestException, SQLException {
        this.commentService.existsById(commentId);
        if (this.commentService.existsVoteForThatCommentFromThisUser(commentId, userId)) {
            throw new BadRequestException(ALREADY_VOTED);
        }
        this.dislikeCommentsDAO.addInThirdTable(commentId, userId);
    }

    @Override
    public void deleteLikeForComment(long commentId, long userId) throws BadRequestException {
        if (this.likeCommentsDAO.delete(commentId, userId) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }

    @Override
    public void deleteDislikeForComment(long commentId, long userId) throws BadRequestException {
        if (this.dislikeCommentsDAO.delete(commentId, userId) == 0) {
            throw new BadRequestException(NOT_ALLOWED_OPERATION);
        }
    }
}
