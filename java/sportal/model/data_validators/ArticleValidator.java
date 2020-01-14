package sportal.model.data_validators;

import sportal.exception.BadRequestException;
import sportal.model.dto.article.ArticleCreateDTO;
import sportal.model.dto.article.ArticleEditDTO;

import static sportal.controller.AbstractController.WRONG_REQUEST;

public class ArticleValidator extends AbstractValidator{


    public static ArticleCreateDTO checkArticleForValidData(ArticleCreateDTO articleCreateDTO) throws BadRequestException {
        if (articleCreateDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (articleCreateDTO.getTitle() == null || articleCreateDTO.getTitle().isEmpty()) {
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
}
