package sportal.model.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class DAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    static  final String SET_FK_FALSE = "SET FOREIGN_KEY_CHECKS=0;";
    static  final String SET_FK_TRUE = "SET FOREIGN_KEY_CHECKS=1;";
    static final String UNSUCCESSFUL_TRANSACTION = "Unsuccessful transaction!";
    static final String UNSUCCESSFUL_CONNECTION_ROLLBACK = "Unsuccessful connection rollback!";

    void setFKFalse() {
        String setFKSQL = "SET FOREIGN_KEY_CHECKS=0;";
        this.jdbcTemplate.update(setFKSQL);
    }

    void setFKTrue() {
        String setFKSQL = "SET FOREIGN_KEY_CHECKS=1;";
        this.jdbcTemplate.update(setFKSQL);
    }
}
