package sportal.controller.models.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentCreateModel {

    private String commentText;
    private long articleId;
}
