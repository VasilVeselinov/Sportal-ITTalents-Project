package sportal.model.data_validators;

import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dto.article.ArticleCreateDTO;
import sportal.model.dto.article.ArticleEditDTO;
import sportal.model.pojo.ExistsObject;

import java.util.List;

public class ArticleValidator extends AbstractValidator {

    public static ArticleCreateDTO checkArticleForValidData(
            ArticleCreateDTO articleCreateDTO) throws BadRequestException {
        if (articleCreateDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (
                articleCreateDTO.getTitle() == null ||
                        articleCreateDTO.getTitle().isEmpty() ||
                        articleCreateDTO.getTitle().length() > MAX_NUMBER_FOR_TITLE_SIZE
        ) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleCreateDTO.getFullText() == null || articleCreateDTO.getFullText().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleCreateDTO.getCategories() == null || articleCreateDTO.getPictures() == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return articleCreateDTO;
    }

    public static ArticleEditDTO validationBeforeEdit(ArticleEditDTO artEditDTO) throws BadRequestException {
        if (artEditDTO == null || artEditDTO.getOldArticleId() < 0) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (artEditDTO.getNewTitle() == null || artEditDTO.getNewTitle().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (artEditDTO.getNewFullText() == null || artEditDTO.getNewFullText().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return artEditDTO;
    }

    public static void validation(List<ExistsObject> objectList, long articleId, long otherId) {
        boolean hasLeftId = false;
        for (ExistsObject object : objectList) {
            if (!hasLeftId && object.getLeftId() != null && object.getLeftId() == articleId) {
                hasLeftId = true;
            }
            if (object.getLeftColumnId() != null && object.getRightColumnId() == otherId) {
                throw new ExistsObjectException(ALREADY_COMBINATION);
            }
        }
        if (!hasLeftId) {
            throw new ExistsObjectException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
    }
}
