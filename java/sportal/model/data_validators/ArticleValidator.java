package sportal.model.data_validators;

import org.springframework.stereotype.Component;
import sportal.exception.BadRequestException;
import sportal.model.dto.article.ArticleCreateDTO;
import sportal.model.dto.article.ArticleEditDTO;

import static sportal.controller.AbstractController.WRONG_REQUEST;

@Component
public class ArticleValidator {


    public ArticleCreateDTO checkArticleForValidData(ArticleCreateDTO articleCreateDTO) throws BadRequestException {
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

    public ArticleEditDTO validationBeforeEdit(ArticleEditDTO artEditDTO) throws BadRequestException {
        if (artEditDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (artEditDTO.getArticleBeforeEditDTO() == null || artEditDTO.getArticleBeforeEditDTO().getId() < 0) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (artEditDTO.getArticleBeforeEditDTO().getOldFullText() == null ||
                artEditDTO.getArticleBeforeEditDTO().getOldFullText().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (artEditDTO.getArticleBeforeEditDTO().getOldTitle() == null ||
                artEditDTO.getArticleBeforeEditDTO().getOldTitle().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (artEditDTO.getNewTitle() == null || artEditDTO.getNewTitle().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (artEditDTO.getNewFullText() == null || artEditDTO.getNewFullText().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        System.out.println("valid");
        return artEditDTO;
    }
}
