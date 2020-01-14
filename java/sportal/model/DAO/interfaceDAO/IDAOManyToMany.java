package sportal.model.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOManyToMany {

    int addInThirdTable(long leftColumn, long rightColumn) throws SQLException;
}
