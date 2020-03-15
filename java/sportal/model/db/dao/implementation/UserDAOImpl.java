package sportal.model.db.dao.implementation;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import sportal.annotations.DAOAnnotation;
import sportal.exception.TransactionException;
import sportal.model.db.dao.DAO;
import sportal.model.db.dao.IUserDAO;
import sportal.model.db.pojo.Role;
import sportal.model.db.pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@DAOAnnotation
public class UserDAOImpl extends DAO implements IUserDAO {

    private static final String ADD_USER =
            "INSERT INTO users (username, password, user_email, is_enabled, token) " +
                    "VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_ROLES_OF_THE_USER =
            "INSERT INTO users_authorities (user_id, authorities_id) " +
                    "VALUES (?, ?);";
    private static final String FIND_ALL_ROLES =
            "SELECT r.id, r.authority " +
                    "FROM roles AS r " +
                    "JOIN users_authorities AS ua ON ua.authorities_id = r.id " +
                    "WHERE ua.user_id = ?;";


    @Override
    public User addUser(User user) throws SQLException {
        Connection connection = this.jdbcTemplate.getDataSource().getConnection();
        try (
                PreparedStatement ps = connection.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psForRoles = connection.prepareStatement(ADD_ROLES_OF_THE_USER)
        ) {
            connection.setAutoCommit(false);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUserEmail());
            ps.setBoolean(4, user.isEnabled());
            ps.setString(5, user.getToken());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getLong(1));
            for (Role role : user.getAuthorities()) {
                psForRoles.setLong(1, user.getId());
                psForRoles.setLong(2, role.getId());
                psForRoles.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new TransactionException(e.getMessage());
            } catch (SQLException ex) {
                throw new SQLException(UNSUCCESSFUL_CONNECTION_ROLLBACK + ex.getMessage());
            }
        } finally {
            connection.setAutoCommit(true);
        }
        return user;
    }

    @Override
    public List<Role> findAllRolesByUserId(long userId) {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(FIND_ALL_ROLES, userId);
        List<Role> roleSet = new ArrayList<>();
        while (rowSet.next()) {
            Role role = new Role();
            role.setId(rowSet.getLong(1));
            role.setAuthority(rowSet.getString(2));
            roleSet.add(role);
        }
        return roleSet;
    }

    @Override
    public void authorise(long userId, long authorityId) {
        this.jdbcTemplate.update(ADD_ROLES_OF_THE_USER, userId, authorityId);
    }
}
