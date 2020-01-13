package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.exception.TransactionException;
import sportal.model.DAO.interfaceDAO.IDAODeleteById;
import sportal.model.pojo.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDAO extends DAO implements IDAODeleteById {

    private static final String DELETE_CATEGORY_SQL = "DELETE FROM categories WHERE id = ?;";
    private static final String UPDATE_CATEGORY_NAME = "UPDATE categories SET category_name = ? WHERE id = ?;";
    private static final String FIND_BY_ID = "SELECT category_name FROM categories WHERE id = ?;";
    private static final String ALL_CATEGORIES_SQL = "SELECT id, category_name FROM categories;";
    private static final String DELETE_FROM_ARTICLES_CATEGORIES_BY_CATEGORY_ID =
            "DELETE FROM articles_categories WHERE category_id = ?;";

    public List<Category> allCategories() throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(ALL_CATEGORIES_SQL);
        List<Category> listFromCategories = new ArrayList<>();
        while (rowSet.next()) {
            listFromCategories.add(this.createCategoryByRowSet(rowSet));
        }
        return listFromCategories;
    }

    private Category createCategoryByRowSet(SqlRowSet rowSet) throws SQLException {
        Category category = new Category();
        category.setId(rowSet.getLong("id"));
        category.setCategoryName(rowSet.getString("category_name"));
        return category;
    }

    public Category editCategory(Category category) throws SQLException {
        if (this.jdbcTemplate.update(UPDATE_CATEGORY_NAME, category.getCategoryName(), category.getId()) > 0){
            return category;
        }
        return null;
    }

    public Category findById(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_BY_ID, id);
        if (rowSet.next()) {
            Category category = new Category();
            category.setId(id);
            category.setCategoryName(rowSet.getString("category_name"));
            return category;
        }
        return null;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (
                PreparedStatement psForSetFKFalse = connection.prepareStatement(SET_FK_FALSE);
                PreparedStatement psForCategory = connection.prepareStatement(DELETE_CATEGORY_SQL);
                PreparedStatement psForAllArticle = connection.prepareStatement(DELETE_FROM_ARTICLES_CATEGORIES_BY_CATEGORY_ID);
                PreparedStatement psForSetFKTrue = connection.prepareStatement(SET_FK_TRUE);
        ) {
            connection.setAutoCommit(false);
            psForSetFKFalse.executeUpdate();
            psForCategory.setLong(1, id);
            psForCategory.executeUpdate();
            psForAllArticle.setLong(1, id);
            psForAllArticle.executeUpdate();
            psForSetFKTrue.executeUpdate();
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
