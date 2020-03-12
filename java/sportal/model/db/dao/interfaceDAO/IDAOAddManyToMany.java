package sportal.model.db.dao.interfaceDAO;

import java.sql.SQLException;

public interface IDAOAddManyToMany {

    void add(long leftColumn, long rightColumn) throws SQLException;
}
