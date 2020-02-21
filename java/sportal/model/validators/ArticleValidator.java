package sportal.model.validators;

import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.pojo.Article;
import sportal.model.db.pojo.ExistsObject;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.ArticleServiceDTO;

import java.util.List;
import java.util.Optional;

public class ArticleValidator extends AbstractValidator {

    public static ArticleServiceDTO checkArticleForValidData(ArticleServiceDTO serviceDTO) throws BadRequestException {
        if (serviceDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        ArticleValidator.isValidTitleAndFullText(serviceDTO.getTitle(),serviceDTO.getFullText());
        if (serviceDTO.getCategories() == null || serviceDTO.getCategories().size() == 0) {
            throw new BadRequestException("Have not a category!");
        }
        if (serviceDTO.getPictures() == null || serviceDTO.getPictures().size() == 0) {
            throw new BadRequestException("Have not a picture!");
        }
        return serviceDTO;
    }

    public static ArticleServiceDTO validationBeforeEdit(ArticleServiceDTO serviceDTO) throws BadRequestException {
        if (serviceDTO == null || serviceDTO.getId() < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        ArticleValidator.isValidTitleAndFullText(serviceDTO.getTitle(),serviceDTO.getFullText());
        return serviceDTO;
    }

    private static void isValidTitleAndFullText(String title, String fulltext) throws BadRequestException {
        if (title == null || title.isEmpty() || title.length() > MAX_NUMBER_FOR_TITLE_SIZE ||
                title.length() < MIN_NUMBER_FOR_TITLE_SIZE) {
            throw new BadRequestException("Title have to between 5 and 100 symbols!");
        }
        if (fulltext == null || fulltext.isEmpty()) {
            throw new BadRequestException("Text field is empty!");
        }
    }

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

    public static void conformityCheck(Optional<Article> existsArticle, User user) throws BadRequestException {
        if (!existsArticle.isPresent()) {
            throw new BadRequestException(THIS_ARTICLE_IS_NOT_EXISTS);
        }
        if (existsArticle.get().getAuthorId() != user.getId()) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHOR);
        }

    }
}
