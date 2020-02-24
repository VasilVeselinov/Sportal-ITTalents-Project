package sportal.model.validators;

import org.springframework.stereotype.Component;
import sportal.exception.AuthorizationException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.pojo.Comment;
import sportal.model.service.dto.UserServiceDTO;

import java.util.Optional;

@Component
public class CommentValidator extends AbstractValidator {

    public static Comment validationOfExistsComment(Optional<Comment> existsComment, UserServiceDTO user) {
        if (!existsComment.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (user.getId() != existsComment.get().getUserId()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        return existsComment.get();
    }
}
