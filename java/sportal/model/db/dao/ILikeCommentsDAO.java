package sportal.model.db.dao;

import sportal.model.db.dao.interfaceDAO.IDAOAddManyToMany;
import sportal.model.db.dao.interfaceDAO.IDAODeleteManyToMany;

import java.sql.SQLException;

public interface ILikeCommentsDAO extends IDAOAddManyToMany, IDAODeleteManyToMany {

    void add(long leftColumn, long rightColumn) throws SQLException;

    int delete(long leftColumn, long rightColumn);
}
