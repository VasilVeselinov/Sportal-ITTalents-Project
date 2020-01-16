package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.dao.interfaceDAO.IDAODeleteById;
import sportal.model.pojo.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDAO extends DAO implements IDAODeleteById {

    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?;";
    private static final String UPDATE_CATEGORY_NAME = "UPDATE categories SET category_name = ? WHERE id = ?;";
    private static final String FIND_CATEGORY_BY_ID = "SELECT id, category_name FROM categories WHERE id = ?;";
    private static final String ALL_CATEGORIES_SQL =
            "SELECT id, category_name " +
                    "FROM categories " +
                    "ORDER BY id;";

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
        if (this.jdbcTemplate.update(UPDATE_CATEGORY_NAME, category.getCategoryName(), category.getId()) > 0) {
            return category;
        }
        return null;
    }

    public Category findById(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_CATEGORY_BY_ID, id);
        if (rowSet.next()) {
            return this.createCategoryByRowSet(rowSet);
        }
        return null;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        this.jdbcTemplate.update(DELETE_CATEGORY, id);
    }
}
