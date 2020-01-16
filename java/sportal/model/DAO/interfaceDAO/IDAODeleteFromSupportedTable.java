package sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAODeleteFromSupportedTable {

   int delete(long leftColumn, long rightColumn) throws SQLException;
}
