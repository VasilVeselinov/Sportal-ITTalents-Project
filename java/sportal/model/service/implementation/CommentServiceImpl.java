package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.CommentDAO;
import sportal.model.service.dto.CommentServiceDTO;
import sportal.model.validators.CommentValidator;
import sportal.model.validators.UserValidator;
import sportal.model.db.pojo.Comment;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.CommentRepository;
import sportal.model.service.ICommentService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static sportal.model.validators.AbstractValidator.*;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleServiceImpl articleService;
    @Autowired
    private CommentDAO commentDAO;

    @Override
    public long addComment(CommentServiceDTO serviceDTO,
                           User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        CommentServiceDTO validComment = CommentValidator.checkForValidDataOfCommentCreateDTO(serviceDTO);
        if (!this.articleService.existsById(validComment.getArticleId())) {
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        Comment comment = new Comment(validComment, logUser.getId());
        comment = this.commentRepository.save(comment);
        comment.setUserName(logUser.getUserName());
        return comment.getArticleId();
    }

    @Override
    public long edit(CommentServiceDTO serviceDTO, User user) throws BadRequestException {
        User logUser = UserValidator.checkUserIsLogged(user);
        CommentServiceDTO validComment = CommentValidator.checkForValidDataOfCommentEditDTO(serviceDTO);
        Optional<Comment> existsComment = this.commentRepository.findById(validComment.getId());
        Comment validExistsComment = CommentValidator.validationOfExistsComment(existsComment, logUser);
        validExistsComment.setFullCommentText(validComment.getText());
        validExistsComment.setDatePublished(Timestamp.valueOf(LocalDateTime.now()));
        Comment editComment = this.commentRepository.save(validExistsComment);
        return editComment.getId();
    }

    @Override
    public long deleteFromUser(long commentId, User user) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        Optional<Comment> existsComment = this.commentRepository.findById(commentId);
        Comment validExistsComment = CommentValidator.validationOfExistsComment(existsComment, logUser);
        this.commentRepository.deleteById(commentId);
        return validExistsComment.getArticleId();
    }

    @Override
    public long deleteFromAdmin(long commentId, User user) throws BadRequestException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<Comment> existsComment = this.commentRepository.findById(commentId);
        if (!existsComment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        this.commentRepository.deleteById(existsComment.get().getId());
        return existsComment.get().getArticleId();
    }

    @Override
    public List<CommentServiceDTO> getAllCommentsByArticleId(long articleId) throws BadRequestException, SQLException {
        if (articleId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        List<Comment> comments = this.commentDAO.allCommentsByArticleId(articleId);
        return CommentServiceDTO.fromPOJOToDTO(comments);
    }

    @Override
    public CommentServiceDTO getCommentsById(long commentId) throws BadRequestException, SQLException {
        if (commentId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        Comment existsComment = this.commentDAO.findById(commentId);
        if (existsComment == null) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        return new CommentServiceDTO(existsComment);
    }

    @Override
    public boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException {
        return this.commentDAO.existsVoteForThatCommentFromThisUser(commentId, userId);
    }

    @Override
    public Optional<Comment> findById(long commentId) {
        return this.commentRepository.findById(commentId);
    }
}