package sportal.model.db.dao.interfaceDAO;

public interface IDAODeleteManyToMany {

   int delete(long leftColumn, long rightColumn);
}
