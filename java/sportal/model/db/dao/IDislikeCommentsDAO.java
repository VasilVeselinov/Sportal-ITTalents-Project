package sportal.model.db.dao;

import sportal.model.db.dao.interfaceDAO.IDAODeleteManyToMany;
import sportal.model.db.dao.interfaceDAO.IDAOAddManyToMany;

import java.sql.SQLException;

public interface IDislikeCommentsDAO extends IDAOAddManyToMany, IDAODeleteManyToMany {

    void add(long leftColumn, long rightColumn) throws SQLException;

    int delete(long leftColumn, long rightColumn);
}
