package sportal.model.validators;

import org.springframework.stereotype.Component;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.controller.models.comment.CommentEditModel;
import sportal.model.db.pojo.Comment;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.CommentServiceDTO;

import java.util.Optional;

@Component
public class CommentValidator extends AbstractValidator {

    public static CommentServiceDTO checkForValidDataOfCommentCreateDTO(
            CommentServiceDTO serviceDTO) throws BadRequestException {
        if (serviceDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (serviceDTO.getArticleId() < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (serviceDTO.getText() == null || serviceDTO.getText().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return serviceDTO;
    }

    public static CommentServiceDTO checkForValidDataOfCommentEditDTO(
            CommentServiceDTO serviceDTO) throws BadRequestException {
        if (serviceDTO == null || serviceDTO.getId() < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (serviceDTO.getText() == null || serviceDTO.getText().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return serviceDTO;
    }

    public static Comment validationOfExistsComment(Optional<Comment> existsComment, User user) {
        if (!existsComment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (user.getId() != existsComment.get().getUserId()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        return existsComment.get();
    }
}
