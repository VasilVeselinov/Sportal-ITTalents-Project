package sportal.model.DAO.interfaceDAO;

import java.sql.SQLException;

public interface IDAOExistsInThirdTable {

    boolean existsInThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
