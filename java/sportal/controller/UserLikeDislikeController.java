package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.model.service.VoteService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UserLikeDislikeController extends AbstractController {

    @Autowired
    private VoteService voteService;

    @PostMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public long likeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                              HttpSession session) throws SQLException, BadRequestException {
        return this.voteService.likeArticle(articleId, session);
    }

    @DeleteMapping(value = "/like_articles/{" + ARTICLE_ID + "}")
    public long removeLikeOfArticle(@PathVariable(name = ARTICLE_ID) long articleId,
                                    HttpSession session) throws BadRequestException {
        return this.voteService.deleteVoteForArticle(articleId, session);
    }

    @PostMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public long likeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                              HttpSession session) throws SQLException, BadRequestException {
        return this.voteService.likeComment(commentId, session);
    }

    @PostMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public long dislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                 HttpSession session) throws SQLException, BadRequestException {
        return this.voteService.dislikeComment(commentId, session);
    }

    @DeleteMapping(value = "/like_comments/{" + COMMENT_ID + "}")
    public long removeLikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                    HttpSession session) throws BadRequestException {
        return this.voteService.deleteLikeForComment(commentId, session);
    }

    @DeleteMapping(value = "/dislike_comments/{" + COMMENT_ID + "}")
    public long removeDislikeOfComment(@PathVariable(name = COMMENT_ID) long commentId,
                                       HttpSession session) throws BadRequestException {
        return this.voteService.deleteDislikeForComment(commentId, session);
    }
}
