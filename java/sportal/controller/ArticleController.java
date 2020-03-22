package sportal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.article.*;
import sportal.controller.models.user.UserLoginModel;
import sportal.exception.BadRequestException;
import sportal.model.service.IArticleService;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.dto.PictureServiceDTO;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static sportal.util.GlobalConstants.HAS_AUTHORITY_ADMIN;
import static sportal.util.GlobalConstants.HAS_AUTHORITY_EDITOR;

@RestController
@RequestMapping(value = "/articles")
public class ArticleController extends AbstractController {

    @Autowired
    private IArticleService articleService;
    private static final Logger LOGGER = LogManager.getLogger(ArticleController.class);

    @PostMapping(value = "/create")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<Void> createArticle(@Valid @RequestBody ArticleCreateModel createModel,
                                              BindingResult bindingResult,
                                              HttpSession session) throws SQLException, BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        LOGGER.info("POST /articles/create");
        ArticleServiceDTO serviceDTO =
                new ArticleServiceDTO(
                        createModel.getTitle(), createModel.getFullText(),
                        CategoryServiceDTO.fromModelToDTO(createModel.getCategories()),
                        PictureServiceDTO.fromModelToDTO(createModel.getPictures()));
        long articleId = this.articleService.addArticle(serviceDTO, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/search")
    public List<ArticleRespModel> searchOfArticlesByTitleOfCategoryName(
            @RequestParam(name = "text", required = false) String titleOrCategory,
            @RequestParam("page") @Positive(message = MASSAGE_FOR_INVALID_NUMBER_OF_PAGE) int page)
            throws SQLException {
        LOGGER.info("GET /articles/search");
        return ArticleRespModel.fromDTOToModel(this.articleService.findByTitleOrCategory(titleOrCategory, page));
    }

    @GetMapping(value = "/{" + ARTICLE_ID + "}")
    public ArticleFullDataModel articleById(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId)
            throws SQLException, BadRequestException {
        LOGGER.info("GET /articles/{" + ARTICLE_ID + "}");
        return new ArticleFullDataModel(this.articleService.findById(articleId));
    }

    @GetMapping(value = "/category/{" + CATEGORY_ID + "}")
    public List<ArticleRespModel> articlesByCategoryId(
            @PathVariable(CATEGORY_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long categoryId,
            @RequestParam("page") @Positive(message = MASSAGE_FOR_INVALID_NUMBER_OF_PAGE) int page)
            throws SQLException, BadRequestException {
        LOGGER.info("GET /articles/category/{" + CATEGORY_ID + "}");
        return ArticleRespModel.fromDTOToModel(this.articleService.findByCategoryId(categoryId, page));
    }

    @GetMapping(value = "/top_5_read_today")
    public List<ArticleRespModel> topFiveViewedArticlesToday() throws SQLException {
        LOGGER.info("GET /articles/top_5_read_today");
        return ArticleRespModel.fromDTOToModel(this.articleService.findTopFiveReadToday());
    }

    @PutMapping(value = "/edit")
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public void editArticleTitleOrText(@Valid @RequestBody ArticleEditModel editModel,
                                       BindingResult bindingResult,
                                       HttpSession session,
                                       HttpServletResponse response) throws BadRequestException, IOException {
        LOGGER.info("PUT /articles/edit");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        ArticleServiceDTO serviceDTO = new ArticleServiceDTO(
                editModel.getOldArticleId(), editModel.getNewTitle(), editModel.getNewFullText());
        response.sendRedirect("/articles/" + this.articleService.edit(serviceDTO, logUser.getId()));
    }

    @DeleteMapping(value = "/{" + ARTICLE_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public ArticleRespModel removeArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId)
            throws BadRequestException {
        LOGGER.info("DELETE /articles/{" + ARTICLE_ID + "}");
        return new ArticleRespModel(this.articleService.delete(articleId));
    }
}
