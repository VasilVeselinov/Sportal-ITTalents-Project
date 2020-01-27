package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Picture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PictureDAO extends DAO {

    private static final String ALL_PICTURES_BY_ARTICLE_ID =
            "SELECT id, picture_url, article_id " +
                    "FROM pictures " +
                    "WHERE article_id = ?;";
    private static final String ALL_PICTURES_WHERE_ARTICLE_ID_IS_NULL =
            "SELECT id, picture_url, article_id " +
                    "FROM pictures " +
                    "WHERE article_id IS NULL;";

    public List<Picture> allPicturesByArticleId(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_PICTURES_BY_ARTICLE_ID, id);
        List<Picture> listWithPictures = new ArrayList<>();
        while (rowSet.next()) {
            listWithPictures.add(this.createPictureByRowSet(rowSet));
        }
        return listWithPictures;
    }

    public List<Picture> AllPicturesWhereArticleIdIsNull() {
        List<Picture> pictureList = new ArrayList<>();
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_PICTURES_WHERE_ARTICLE_ID_IS_NULL);
        while (rowSet.next()) {
            pictureList.add(this.createPictureByRowSet(rowSet));
        }
        return pictureList;
    }

    private Picture createPictureByRowSet(SqlRowSet rowSet) {
        Picture picture = new Picture();
        picture.setId(rowSet.getLong("id"));
        picture.setUrlOFPicture(rowSet.getString("picture_url"));
        picture.setArticleId(rowSet.getLong("article_id"));
        return picture;
    }
}
