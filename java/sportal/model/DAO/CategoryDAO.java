package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.pojo.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDAO extends DAO{

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
}
