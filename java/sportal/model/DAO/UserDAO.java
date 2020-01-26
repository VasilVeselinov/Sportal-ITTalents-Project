package sportal.model.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import sportal.model.dao.interfaceDAO.IDAODeleteById;
import sportal.model.pojo.User;

import java.sql.*;

@Component
public class UserDAO extends DAO implements IDAODeleteById {

    private static final String INSERT_USER =
            "INSERT INTO users (user_name, user_password, user_email, is_admin) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_PASSWORD = "UPDATE users SET user_password = ? WHERE id = ?;";
    private static final String FIND_USER_BY_USER_ID =
            "SELECT id, user_name, user_password, user_email, is_admin FROM users WHERE id = ?;";
    private static final String FIND_USER_BY_USER_NAME_OR_EMAIL =
            "SELECT id, user_name, user_password, user_email, is_admin " +
                    "FROM users " +
                    "WHERE user_name = ? " +
                    "OR user_email = ?;";
    private static final String FIND_USER_BY_USER_NAME =
            "SELECT id, user_name, user_password, user_email, is_admin " +
                    "FROM users" +
                    " WHERE user_name = ?;";

    public User add(User user) throws SQLException {
        try (
                Connection connection = this.jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserPassword());
            ps.setString(3, user.getUserEmail());
            ps.setBoolean(4, user.getIsAdmin());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getLong(1));
            return user;
        }
    }

    private User createUserByRowSet(SqlRowSet rowSet) {
        User user = new User();
        user.setId(rowSet.getLong("id"));
        user.setUserName(rowSet.getString("user_name"));
        user.setUserPassword(rowSet.getString("user_password"));
        user.setUserEmail(rowSet.getString("user_email"));
        user.setIsAdmin(rowSet.getBoolean("is_admin"));
        return user;
    }

    public User findUserByUserId(long id) throws SQLException {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_USER_BY_USER_ID, id);
        if (rowSet.next()) {
            return this.createUserByRowSet(rowSet);
        }
        return null;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        this.setFKFalse();
        String deleteSQL = "DELETE FROM users WHERE id = ?;";
        this.jdbcTemplate.update(deleteSQL, id);
        this.setFKTrue();
    }
}
