package sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOExistsInThirdTable {

    boolean existsInThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
