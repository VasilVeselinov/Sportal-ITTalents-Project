package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.article.*;
import sportal.exception.BadRequestException;
import sportal.model.db.pojo.User;
import sportal.model.service.IArticleService;
import sportal.model.service.dto.ArticleServiceDTO;
import sportal.model.service.dto.CategoryServiceDTO;
import sportal.model.service.dto.PictureServiceDTO;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ArticleController extends AbstractController {

    @Autowired
    private IArticleService articleService;

    @PostMapping(value = "/articles")
    public ResponseEntity<Void> createArticle(
            @RequestBody ArticleCreateModel articleModel,
            HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        ArticleServiceDTO serviceDTO =
                new ArticleServiceDTO(
                        articleModel.getTitle(), articleModel.getFullText(),
                        CategoryServiceDTO.fromModelToDTO(articleModel.getCategories()),
                        PictureServiceDTO.fromModelToDTO(articleModel.getPictures()));
        long articleId = this.articleService.addArticle(serviceDTO, user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/articles/" + articleId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping(value = "/articles/search")
    public List<ArticleRespModel> searchOfArticlesByTitleOfCategoryName(
            @RequestParam(name = "text", required = false) String titleOrCategory) throws SQLException {
        return ArticleRespModel.fromDTOToModel(this.articleService.findByArticleTitleOrCategory(titleOrCategory));
    }

    @GetMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public ArticleFullDataModel articleById(
            @PathVariable(name = ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        return new ArticleFullDataModel(this.articleService.findArticleById(articleId));
    }

    @GetMapping(value = "/articles/the_category/{" + CATEGORY_ID + "}")
    public List<ArticleRespModel> articlesByCategoryId(
            @PathVariable(CATEGORY_ID) long categoryId) throws SQLException, BadRequestException {
        return ArticleRespModel.fromDTOToModel(this.articleService.findByCategoryId(categoryId));
    }

    @GetMapping(value = "/articles/top_5_read_today")
    public List<ArticleRespModel> topFiveViewedArticlesToday() throws SQLException {
        return ArticleRespModel.fromDTOToModel(this.articleService.findTopFiveReadToday());
    }


    @PutMapping(value = "/articles")
    public void editArticleTitleOrText(
            @RequestBody ArticleEditModel articleEditDTO,
            HttpSession session,
            HttpServletResponse response) throws BadRequestException, IOException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        ArticleServiceDTO serviceDTO = new ArticleServiceDTO(
                articleEditDTO.getOldArticleId(), articleEditDTO.getNewTitle(), articleEditDTO.getNewFullText());
        response.sendRedirect("/articles/" + this.articleService.edit(serviceDTO, user));
    }

    @DeleteMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public ArticleRespModel removeArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                          HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new ArticleRespModel(this.articleService.delete(articleId, user));
        // vasko: response.sendRedirect("/home");
    }
}
