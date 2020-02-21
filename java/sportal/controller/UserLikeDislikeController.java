package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.model.pojo.User;
import sportal.model.service.IVoteService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserLikeDislikeController extends AbstractController {

    @Autowired
    private IVoteService voteService;

    @PostMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public long likeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                              HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.voteService.likeArticle(articleId, user);
    }

    @DeleteMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public long removeLikeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                    HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.voteService.deleteVoteForArticle(articleId, user);
    }

    @PostMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public long likeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                              HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.voteService.likeComment(commentId, user);
    }

    @PostMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public long dislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                 HttpSession session) throws SQLException, BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.voteService.dislikeComment(commentId, user);
    }

    @DeleteMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public long removeLikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                    HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.voteService.deleteLikeForComment(commentId, user);
    }

    @DeleteMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public long removeDislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                       HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.voteService.deleteDislikeForComment(commentId, user);
    }
}
