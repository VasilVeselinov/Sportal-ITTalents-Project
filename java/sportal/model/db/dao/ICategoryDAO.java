package sportal.model.db.dao;

import sportal.model.db.dao.interfaceDAO.IDAOAddManyToMany;
import sportal.model.db.dao.interfaceDAO.IDAODeleteManyToMany;
import sportal.model.db.pojo.Category;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDAO extends IDAOAddManyToMany, IDAODeleteManyToMany {

    List<Category> findAllByArticleId(long articleID) throws SQLException;

    boolean existsCombination(long articleId, long categoryId) throws SQLException;

    void add(long leftColumn, long rightColumn) throws SQLException;

    int delete(long leftColumn, long rightColumn);
}
