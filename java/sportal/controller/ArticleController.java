package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.BadRequestException;
import sportal.model.dto.article.*;
import sportal.model.pojo.User;
import sportal.model.service.ArticleService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ArticleController extends AbstractController {

    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/articles")
    public ArticleAfterCreateDTO createArticle(@RequestBody ArticleCreateDTO articleCreateDTO,
                                               HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.articleService.addArticle(articleCreateDTO, user);
    }

    @GetMapping(value = "/articles/search")
    public List<ArticleRespDTO> searchOfArticlesByTitleOfCategoryName(
            @RequestParam(name = "text", required = false) String titleOrCategory) throws SQLException {
        return this.articleService.findByArticleTitleOrCategory(titleOrCategory);
    }

    @GetMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public ArticleFullDataDTO articleById(
            @PathVariable(name = ARTICLE_ID) long articleId) throws SQLException, BadRequestException {
        return this.articleService.findArticleById(articleId);
    }

    @GetMapping(value = "/articles/the_category/{" + CATEGORY_ID + "}")
    public List<ArticleRespDTO> articlesByCategoryId(
            @PathVariable(CATEGORY_ID) long categoryId) throws SQLException, BadRequestException {
        return this.articleService.findByCategoryId(categoryId);
    }

    @GetMapping(value = "/articles/top_5_read_today")
    public List<ArticleRespDTO> topFiveViewedArticlesToday() throws SQLException {
        return this.articleService.findTopFiveReadToday();
    }


    @PutMapping(value = "/articles")
    public ArticleAfterEditDTO editArticleTitleOrText(@RequestBody ArticleEditDTO articleEditDTO,
                                                      HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.articleService.edit(articleEditDTO, user);
    }

    @DeleteMapping(value = "/articles/{" + ARTICLE_ID + "}")
    public ArticleRespDTO removeArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                        HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.articleService.delete(articleId, user);
    }
}
