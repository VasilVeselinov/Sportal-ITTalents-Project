package sportal.model.db.dao;

import sportal.model.db.pojo.Role;
import sportal.model.db.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    User addUser(User user) throws SQLException;

    List<Role> findAllRolesByUserId(long userId);

    void upAuthorityByUserId(long userId, long authorityId);
}
