package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sportal.controller.util.AuthValidator;
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

@RestController
@RequestMapping(value = "/articles")
public class ArticleController extends AbstractController {

    @Autowired
    private IArticleService articleService;

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createArticle(@Valid @RequestBody ArticleCreateModel articleModel,
                                              BindingResult bindingResult,
                                              HttpSession session) throws SQLException, BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsAdmin(logUser);
        ArticleServiceDTO serviceDTO =
                new ArticleServiceDTO(
                        articleModel.getTitle(), articleModel.getFullText(),
                        CategoryServiceDTO.fromModelToDTO(articleModel.getCategories()),
                        PictureServiceDTO.fromModelToDTO(articleModel.getPictures()));
        long articleId = this.articleService.addArticle(serviceDTO, logUser.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/search")
    public List<ArticleRespModel> searchOfArticlesByTitleOfCategoryName(
            @RequestParam(name = "text", required = false) String titleOrCategory) throws SQLException {
        return ArticleRespModel.fromDTOToModel(this.articleService.findByArticleTitleOrCategory(titleOrCategory));
    }

    @GetMapping(value = "/{" + ARTICLE_ID + "}")
    public ArticleFullDataModel articleById(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId)
            throws SQLException, BadRequestException {
        return new ArticleFullDataModel(this.articleService.findArticleById(articleId));
    }

    @GetMapping(value = "/the_category/{" + CATEGORY_ID + "}")
    public List<ArticleRespModel> articlesByCategoryId(
            @PathVariable(CATEGORY_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long categoryId)
            throws SQLException, BadRequestException {
        return ArticleRespModel.fromDTOToModel(this.articleService.findByCategoryId(categoryId));
    }

    @GetMapping(value = "/top_5_read_today")
    public List<ArticleRespModel> topFiveViewedArticlesToday() throws SQLException {
        return ArticleRespModel.fromDTOToModel(this.articleService.findTopFiveReadToday());
    }


    @PutMapping(value = "/edit")
    public void editArticleTitleOrText(@Valid @RequestBody ArticleEditModel articleEditDTO,
                                       BindingResult bindingResult,
                                       HttpSession session,
                                       HttpServletResponse response) throws BadRequestException, IOException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsAdmin(logUser);
        ArticleServiceDTO serviceDTO = new ArticleServiceDTO(
                articleEditDTO.getOldArticleId(), articleEditDTO.getNewTitle(), articleEditDTO.getNewFullText());
        response.sendRedirect("/articles/" + this.articleService.edit(serviceDTO, logUser.getId()));
    }

    @DeleteMapping(value = "/{" + ARTICLE_ID + "}")
    public ArticleRespModel removeArticle(
            @PathVariable(name = ARTICLE_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long articleId,
            HttpSession session) throws BadRequestException {
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        AuthValidator.checkUserIsEditor(logUser);
        return new ArticleRespModel(this.articleService.delete(articleId));
    }
}
