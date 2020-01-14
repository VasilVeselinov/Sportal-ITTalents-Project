package sportal.model.data_validators;

import org.springframework.stereotype.Component;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.model.dto.comment.CommentCreateDTO;
import sportal.model.dto.comment.CommentEditDTO;
import sportal.model.pojo.Comment;
import sportal.model.pojo.User;

import static sportal.controller.AbstractController.WRONG_REQUEST;

@Component
public class CommentValidator extends AbstractValidator {

    public static CommentCreateDTO checkForValidDataOfCommentCreateDTO(CommentCreateDTO commentCreateDTO) throws BadRequestException {
        if (commentCreateDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (commentCreateDTO.getArticleId() < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (commentCreateDTO.getCommentText() == null || commentCreateDTO.getCommentText().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return commentCreateDTO;
    }

    public static CommentEditDTO checkForValidDataOfCommentEditDTO(CommentEditDTO commentEditDTO) throws BadRequestException {
        if (commentEditDTO == null || commentEditDTO.getOldCommentId() < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (commentEditDTO.getFullCommentText() == null || commentEditDTO.getFullCommentText().isEmpty()){
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (commentEditDTO.getNewTextOfComment()== null || commentEditDTO.getNewTextOfComment().isEmpty()){
            throw new BadRequestException(WRONG_REQUEST);
        }
        return commentEditDTO;
    }

    public static Comment validationOfExistsComment(Comment existsComment, User user) throws BadRequestException {
        if (existsComment == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (user.getId() != existsComment.getUserId()) {
            throw new AuthorizationException(WRONG_INFORMATION);
        }
        return existsComment;
    }
}
