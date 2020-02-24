package sportal.model.validators;

import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.pojo.Article;
import sportal.model.db.pojo.ExistsObject;
import sportal.model.service.dto.UserServiceDTO;

import java.util.List;
import java.util.Optional;

public class ArticleValidator extends AbstractValidator {

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

    public static void conformityCheck(Optional<Article> existsArticle, UserServiceDTO user) throws BadRequestException {
        if (!existsArticle.isPresent()) {
            throw new BadRequestException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        if (existsArticle.get().getAuthorId() != user.getId()) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }
    }
}
