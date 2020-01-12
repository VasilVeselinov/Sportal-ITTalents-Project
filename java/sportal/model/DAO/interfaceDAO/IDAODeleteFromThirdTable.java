package sportal.model.DAO.interfaceDAO;

import java.sql.SQLException;

public interface IDAODeleteFromThirdTable {

   int deleteFromThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
