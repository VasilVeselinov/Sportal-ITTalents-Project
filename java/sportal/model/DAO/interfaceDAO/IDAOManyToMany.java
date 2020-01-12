package sportal.model.DAO.interfaceDAO;

import java.sql.SQLException;

public interface IDAOManyToMany {

    int addInThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
