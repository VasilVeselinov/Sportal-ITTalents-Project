package sportal.model.db.dao;

import sportal.model.db.pojo.Comment;

import java.sql.SQLException;
import java.util.List;

public interface ICommentDAO {

    Comment findById(long commentId) throws SQLException;

    List<Comment> allCommentsByArticleId(long articleId) throws SQLException;

    boolean isCommentLikedOrDislikedByUser(long commentId, long userId) throws SQLException;
}
