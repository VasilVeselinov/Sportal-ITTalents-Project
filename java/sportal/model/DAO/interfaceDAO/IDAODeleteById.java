package sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAODeleteById {

    void deleteById(long id) throws SQLException;
}
