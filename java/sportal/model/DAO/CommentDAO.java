package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.DAO.interfaceDAO.IDAODeleteById;
import sportal.model.pojo.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentDAO extends DAO implements IDAODeleteById {

    private static final String INSERT_COMMENT =
            "INSERT INTO comments (full_comment_text , date_published, user_id, article_id) " +
                    "VALUES (?, ?, ?, ?);";
    private static final String UPDATE_COMMENT_TEXT_BY_ID = "UPDATE comments SET full_comment_text = ? WHERE id = ?;";
    private static final String DELETE_COMMENT_BY_ID = "DELETE FROM comments WHERE id = ?;";
    private static final String FIND_COMMENT_BY_ID =
            "SELECT c.id, c.full_comment_text, c.date_published, c.user_id, c.article_id, u.user_name " +
                    "FROM comments AS c " +
                    "LEFT JOIN users AS u ON c.user_id = u.id " +
                    "WHERE id = ?;";
    private static final String ALL_COMMENT_BY_ARTICLE_ID =
            "SELECT c.id, c.full_comment_text, c.date_published, c.user_id, c.article_id, u.user_name, " +
                    "COUNT(ulc.user_id) AS number_of_likes, " +
                    "COUNT(udc.user_id) AS number_of_dislikes " +
                    "FROM comments AS c " +
                    "LEFT JOIN users AS u ON c.user_id = u.id " +
                    "LEFT JOIN users_like_comments AS ulc ON ulc.comment_id = c.id " +
                    "LEFT JOIN users_disliked_comments AS udc ON udc.comment_id = c.id " +
                    "GROUP BY c.id " +
                    "HAVING c.article_id = ?;";

    public Comment addCommentToArticle(Comment comment) throws SQLException {
        try (
                Connection connection = this.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, comment.getFullCommentText());
            ps.setTimestamp(2, comment.getDatePublished());
            ps.setLong(3, comment.getUserId());
            ps.setLong(4, comment.getArticleId());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            comment.setId(resultSet.getLong(1));
            return comment;
        }
    }

    public Comment editComment(Comment comment) throws SQLException {
        if (this.jdbcTemplate.update(UPDATE_COMMENT_TEXT_BY_ID, comment.getFullCommentText(), comment.getId()) > 0){
            return comment;
        }
        return null;
    }

    public Comment findCommentById(long commentId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_COMMENT_BY_ID, commentId);
        if (rowSet.next()) {
            return this.createCommentByRowSet(rowSet);
        }
        return null;
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

    public List<Comment> allCommentsByArticleId(long articleId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_COMMENT_BY_ARTICLE_ID, articleId);
        List<Comment> listWithComments = new ArrayList<>();
        while (rowSet.next()) {
            listWithComments.add(this.createCommentByRowSet(rowSet));
        }
        return listWithComments;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        this.jdbcTemplate.update(DELETE_COMMENT_BY_ID, id);
    }
}
