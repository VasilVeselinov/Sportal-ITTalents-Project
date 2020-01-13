package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.exception.TransactionException;
import sportal.model.pojo.Picture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PictureDAO extends DAO {

    private static final String UPLOAD_PICTURE = "INSERT INTO pictures (picture_url) VALUES (?);";
    private static final String DELETE_PICTURE_BY_ID = "DELETE FROM pictures WHERE id = ?;";
    private static final String FIND_PICTURE_BY_ID =
            "SELECT picture_url FROM pictures WHERE id = ?;";
    private static final String UPDATE_ARTICLE_ID_INTO_PICTURES_TABLE =
            "UPDATE pictures SET article_id = ? WHERE id = ?;";
    private static final String ALL_PICTURES_BY_ARTICLE_ID =
            "SELECT id, picture_url FROM pictures WHERE article_Id = ?;";

    public List<Picture> uploadOfPictures(List<Picture> pictures) throws SQLException {
        List<Picture> pictureList = new ArrayList<>();
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPLOAD_PICTURE, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            for (Picture picture : pictures) {
                statement.setString(1, picture.getUrlOFPicture());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                picture.setId(resultSet.getLong(1));
                pictureList.add(picture);
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new TransactionException(UNSUCCESSFUL_TRANSACTION + e.getMessage());
            } catch (SQLException ex) {
                throw new SQLException(UNSUCCESSFUL_CONNECTION_ROLLBACK + ex.getMessage());
            }
        } finally {
            connection.setAutoCommit(true);
        }
        return pictureList;
    }

    public int deletePictureById(long id) throws SQLException {
        return this.jdbcTemplate.update(DELETE_PICTURE_BY_ID, id);
    }

    public List<Picture> allPicturesByArticleId(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_PICTURES_BY_ARTICLE_ID, id);
        List<Picture> listWithPictures = new ArrayList<>();
        while (rowSet.next()) {
            listWithPictures.add(this.createPictureByRowSet(rowSet));
        }
        return listWithPictures;
    }

    private Picture createPictureByRowSet(SqlRowSet rowSet) {
        Picture picture = new Picture();
        picture.setId(rowSet.getLong("id"));
        picture.setUrlOFPicture(rowSet.getString("picture_url"));
        return picture;
    }

    public Picture findPictureById(long pictureId) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_PICTURE_BY_ID, pictureId);
        if (rowSet.next()) {
            Picture picture = new Picture();
            picture.setId(pictureId);
            picture.setUrlOFPicture(rowSet.getString("picture_url"));
            return picture;
        }
        return null;
    }

    public void addArticleIdToAllPictures(List<Picture> pictures, long articleId) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ARTICLE_ID_INTO_PICTURES_TABLE)) {
            connection.setAutoCommit(false);
            for (Picture p : pictures) {
                statement.setLong(2, p.getId());
                statement.setLong(1, articleId);
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new TransactionException(UNSUCCESSFUL_TRANSACTION);
            } catch (SQLException ex) {
                throw new SQLException(UNSUCCESSFUL_CONNECTION_ROLLBACK);
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
