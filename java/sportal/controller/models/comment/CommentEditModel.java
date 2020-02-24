package sportal.controller.models.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.validation.TextValidation;

import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@Setter
public class CommentEditModel {

    @Positive(message = "Id must be greater than 0!")
    private long oldCommentId;
    @TextValidation
    private String newTextOfComment;
}
