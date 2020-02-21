package sportal.controller.models.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentEditModel {

    private long oldCommentId;
    private String newTextOfComment;
}
