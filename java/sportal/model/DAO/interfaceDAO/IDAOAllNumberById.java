package sportal.model.DAO.interfaceDAO;

import java.sql.SQLException;

public interface IDAOAllNumberById {

    int allById(long id) throws SQLException;
}
