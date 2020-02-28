package sportal.model.validators;

import sportal.exception.ExistsObjectException;
import sportal.model.db.pojo.ExistsObject;

import java.util.List;

public class ArticleValidator {

    private static final String ALREADY_COMBINATION = "Exists this combination!";
    private static final String THIS_ARTICLE_IS_NOT_EXISTS = "This article is not exists!";

    public static void validation(List<ExistsObject> objectList, long articleId, long userId) {
        boolean hasLeftId = false;
        for (ExistsObject object : objectList) {
            if (!hasLeftId && object.getLeftId() != null && object.getLeftId() == articleId) {
                hasLeftId = true;
            }
            if (object.getLeftColumnId() != null && object.getRightColumnId() == userId) {
                throw new ExistsObjectException(ALREADY_COMBINATION);
            }
        }
        if (!hasLeftId) {
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
    }
}
