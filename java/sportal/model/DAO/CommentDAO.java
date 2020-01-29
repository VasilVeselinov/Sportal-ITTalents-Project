package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentDAO extends DAO {

    private static final String FIND_COMMENT_BY_ID =
            "SELECT c.id, c.full_comment_text, c.date_published, c.user_id, c.article_id, u.user_name, " +
                    "COUNT(ulc.user_id) AS number_of_likes, " +
                    "COUNT(udc.user_id) AS number_of_dislikes " +
                    "FROM comments AS c " +
                    "LEFT JOIN users AS u ON c.user_id = u.id " +
                    "LEFT JOIN users_like_comments AS ulc ON ulc.comment_id = c.id " +
                    "LEFT JOIN users_disliked_comments AS udc ON udc.comment_id = c.id " +
                    "GROUP BY c.id " +
                    "HAVING c.id = ?;";
    private static final String ALL_COMMENT_BY_ARTICLE_ID =
            "SELECT c.id, c.full_comment_text, c.date_published, c.user_id, c.article_id, u.user_name, " +
                    "COUNT(ulc.user_id) AS number_of_likes, " +
                    "COUNT(udc.user_id) AS number_of_dislikes " +
                    "FROM comments AS c " +
                    "LEFT JOIN users AS u ON c.user_id = u.id " +
                    "LEFT JOIN users_like_comments AS ulc ON ulc.comment_id = c.id " +
                    "LEFT JOIN users_disliked_comments AS udc ON udc.comment_id = c.id " +
                    "GROUP BY c.id " +
                    "HAVING c.article_id = ? " +
                    "ORDER BY c.date_published DESC;";
    private static final String EXISTS_VOTED_COMMENT =
            "SELECT c.id " +
                    "FROM comments AS c " +
                    "LEFT JOIN users_like_comments AS uls ON uls.comment_id = c.id " +
                    "LEFT JOIN users_disliked_comments AS udc ON udc.comment_id = c.id " +
                    "WHERE (uls.comment_id = ? AND uls.user_id = ?) OR (udc.comment_id = ? AND udc.user_id = ?);";

    public Comment findCommentById(long commentId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_COMMENT_BY_ID, commentId);
        if (rowSet.next()) {
            return this.createCommentByRowSet(rowSet);
        }
        return null;
    }

    public List<Comment> allCommentsByArticleId(long articleId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_COMMENT_BY_ARTICLE_ID, articleId);
        List<Comment> listWithComments = new ArrayList<>();
        while (rowSet.next()) {
            listWithComments.add(this.createCommentByRowSet(rowSet));
        }
        return listWithComments;
    }

    private Comment createCommentByRowSet(SqlRowSet rowSet) {
        Comment comment = new Comment();
        comment.setId(rowSet.getLong("id"));
        comment.setFullCommentText(rowSet.getString("full_comment_text"));
        comment.setDatePublished(rowSet.getTimestamp("date_published"));
        comment.setUserId(rowSet.getLong("user_id"));
        comment.setArticleId(rowSet.getLong("article_id"));
        comment.setUserName(rowSet.getString("user_name"));
        comment.setNumberOfLikes(rowSet.getInt("number_of_likes"));
        comment.setNumberOfDislike(rowSet.getInt("number_of_dislikes"));
        return comment;
    }

    public boolean existsVoteForThatCommentFromThisUser(long commentId, long userId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(EXISTS_VOTED_COMMENT,
                commentId, userId, commentId, userId);
        return rowSet.next();
    }
}
